import "./SearchResult.css";
import profileUserImg from "../../../../assests/woman.jpg";
import { useNavigate } from "react-router-dom";
export const SearchResult = ({ result }) => {
  const navigate = useNavigate();
  console.log(result);
  const handleProfile = () => {
    const userInfoDTOWithUser = {
      ...result.userInfoDTO,
      user: result.userInfoDTO.employee,
    };

    delete userInfoDTOWithUser.employee;

    const queryParams = new URLSearchParams({
      emailId: result.userInfoDTO.emailId,
      user: encodeURIComponent(JSON.stringify(userInfoDTOWithUser)),
    });
    sessionStorage.setItem("isFriend", result.isFriend);
    navigate(`/profile?${queryParams.toString()}`);
  };

  return (
    <div className="search-result" onClick={handleProfile}>
      <div>
        <img src={profileUserImg} className="profileUserImg" />
      </div>

      <div className="info">
        {result.userInfoDTO.employee.userName}
        <div className="accName">
          @{result.userInfoDTO.accName} {result.isFriend ? ",bạn bè" : ""}
        </div>

        <div className="accName">{result.mutualFriend + " bạn chung"}</div>
      </div>
    </div>
  );
};
