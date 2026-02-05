import { useEffect, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { API_BASE_URL } from './config'

const OAuth2Callback = () => {
  const hasFetched = useRef(false); // 실행 여부 기록
  // 폼 제출 후 리다이렉트, 조건부 라우팅, 뒤로가기
  const navigate = useNavigate();
  // 현재 경로 기반 조건부 렌더링, 쿼리 파라미터 파싱, 페이지 간 데이터 전달 확인
  const location = useLocation();
  useEffect(() => {
    const fetchCallback = async () => {
      // 이미 실행 중이거나 성공했다면 중단
      if (hasFetched.current) return;
      hasFetched.current = true;      
      const params = new URLSearchParams(location.search);
      const code = params.get("code");
      console.log("code", code);
      try {
        const response = await fetch(`${API_BASE_URL}/api/jwtcallback`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({code})
        });
        if (response.ok) {
          const jwtToken = response.headers.get("Authorization");
          console.log("jwtToken", jwtToken);
          if (jwtToken) sessionStorage.setItem("jwtToken", jwtToken);
          console.log("로그인 성공!");
          navigate("/welcome");
        } else {
          alert("JWT 검증 실패");
          navigate("/login");
        }
      } catch (err) {
        console.error("서버 요청 오류", err);
        alert("서버 요청 오류");
        navigate("/login");
      }
    };
    fetchCallback();
  }, [navigate, location]);
  return <p>로그인 처리중...</p>;
};
export default OAuth2Callback;
