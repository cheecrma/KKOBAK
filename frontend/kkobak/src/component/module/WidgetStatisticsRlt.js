import React, { useEffect, useState } from "react";
import Box from "@mui/material/Box";
import { requestMyChallengeStatistics } from "api/userApi";
import char from "../../static/char.png";
import smile from "../../static/emoji/smile.png";
import cry from "../../static/emoji/cry.png";

export default function WidgetStatsRlt() {
  const [stats, setStats] = useState([]);

  useEffect(() => {
    requestMyChallengeStatistics(
      requestMyChallengeStatisticsSuccess,
      requestMyChallengeStatisticsFail
    );
  }, []);

  function requestMyChallengeStatisticsSuccess(res) {
    setStats(res.data);
  }

  function requestMyChallengeStatisticsFail(err) {
    setStats([]);
  }

  return (
    <Box
      sx={{
        width: "280px",
        height: "130px",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        backgroundColor: "white",
        borderRadius: 2,
      }}
    >
      <Box sx={{ flex: 1, display: "flex", flexDirection: "column" }}>
        <div
          className="neon pink"
          style={{
            fontFamily: "alarm_clock",
            fontSize: "18px",
            flex: 1,
            float: "left",
          }}
        >
          <Box
            sx={{
              flex: 1,
              float: "left",
            }}
          >
            완료한 챌린지 : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          </Box>
          <img src={char} style={{ width: "30px", float: "left" }} />
          &nbsp;×
          {stats.finChl}
        </div>
        <div
          className="neon pink"
          style={{
            fontFamily: "alarm_clock",
            fontSize: "18px",
            flex: 1,
            float: "left",
          }}
        >
          <Box
            sx={{
              flex: 1,
              float: "left",
            }}
          >
            성공한 챌린지 : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          </Box>
          <img src={smile} style={{ width: "30px", float: "left" }} />
          &nbsp;×
          {stats.sucChl}
        </div>
        <div
          className="neon pink"
          style={{
            fontFamily: "alarm_clock",
            fontSize: "18px",
            flex: 1,
            float: "left",
          }}
        >
          <Box
            sx={{
              flex: 1,
              float: "left",
            }}
          >
            실패한 챌린지 : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          </Box>
          <img src={cry} style={{ width: "30px", float: "left" }} />
          &nbsp;×
          {stats.failChl}
        </div>
      </Box>
    </Box>
  );
}
