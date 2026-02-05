import { useNavigate } from "react-router-dom";
const WelcomePage = () => {
  const navigate = useNavigate();
  const logout = () => {
    if (window.confirm("정말 로그아웃 하시겠습니까?")) {
      sessionStorage.removeItem("jwtToken");
      sessionStorage.removeItem("username");
      alert("로그아웃 되었습니다.");
      navigate("/login");
    }
  };
  return (
    <div className="welcome-container">
      <div className="header">
        <h1 id="welcomeMessage">환영합니다!</h1>
        <button className="logout-btn" onClick={logout}>로그아웃</button>
      </div>
    </div>
  );
};
export default WelcomePage;
