import { HashRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./LoginPage";
import WelcomePage from "./WelcomePage";
import OAuth2Callback from "./OAuth2Callback";
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/welcome" element={<WelcomePage />} />
        <Route path="/callback" element={<OAuth2Callback />} />
        <Route path="*" element={<LoginPage />} />
      </Routes>
    </Router>
  );
}
export default App;
