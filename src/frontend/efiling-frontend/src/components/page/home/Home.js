import React, { useState } from "react";
import PropTypes from "prop-types";
import queryString from "query-string";
import { useLocation } from "react-router-dom";
import axios from "axios";
import { Header, Footer, Loader } from "shared-components";
import CSOStatus from "../../composite/cso-status/CSOStatus";
import CSOAccount from "../cso-account/CSOAccount";
import { propTypes } from "../../../types/propTypes";

import "../page.css";

export const saveNavigationToSession = ({ cancel, success, error }) => {
  if (cancel.url) sessionStorage.setItem("cancelUrl", cancel.url);
  if (success.url) sessionStorage.setItem("successUrl", success.url);
  if (error.url) sessionStorage.setItem("errorUrl", error.url);
};

// make call to submission/{id} to get the user and navigation details
const checkCSOAccountStatus = (
  submissionId,
  setCsoAccountExists,
  setShowLoader
) => {
  axios
    .get(`/submission/${submissionId}`)
    .then(({ data: { userDetails, navigation } }) => {
      saveNavigationToSession(navigation);

      if (userDetails.accounts) {
        const csoAccountExists = userDetails.accounts.some(
          element => element.type === "CSO"
        );
        if (csoAccountExists) setCsoAccountExists(true);
      }
      setShowLoader(false);
    })
    .catch(() => {});
};

export default function Home({ page: { header, confirmationPopup } }) {
  const [showLoader, setShowLoader] = useState(false);
  const [csoAccountExists, setCsoAccountExists] = useState(false);
  const location = useLocation();
  const queryParams = queryString.parse(location.search);

  checkCSOAccountStatus(
    queryParams.submissionId,
    setCsoAccountExists,
    setShowLoader
  );

  const csoStatus = {
    accountExists: csoAccountExists,
    confirmationPopup
  };

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
          {showLoader && <Loader page />}
          {!showLoader && !csoAccountExists && (
            <CSOAccount confirmationPopup={confirmationPopup} />
          )}
          {!showLoader && csoAccountExists && (
            <CSOStatus csoStatus={csoStatus} />
          )}
        </div>
      </div>
      <Footer />
    </main>
  );
}

Home.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
    confirmationPopup: propTypes.confirmationPopup
  }).isRequired
};
