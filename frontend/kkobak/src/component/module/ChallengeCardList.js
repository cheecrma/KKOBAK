import React, { useEffect, useState } from "react";
import Box from "@mui/material/Box";
import styled from "@emotion/styled";
import Pagination from "@mui/material/Pagination";
import Stack from "@mui/material/Stack";

import ChallengeCard from "component/module/ChallengeCard";
import Button from "component/atom/TextButton";
import Text from "component/atom/Text";
import MainBox from "component/atom/MainBox";
import WatchToggle from "component/atom/WatchToggle";

import { fetchChallengeList, fetchChallengePageCnt } from "api/Challenge";

const WatchToggleBox = styled(Box)(
  () => `
  width: 90%;
  margin: 20px 20px 0 20px;
  display: flex;
  justify-content: start
  `
);

const ChallengeListBox = styled(Box)(
  () => `
  width: 100%;
  height: 70vh;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  `
);

const ButtonBox = styled(Box)(
  () => `
  width: 95%;
  display: flex;
  justify-content: end;
  `
);

const PageBox = styled(Box)(
  () => `
  margin: 10px auto;
  `
);

export default function ChallengeCardList() {
  const [ChallengeList, setChallengeList] = useState([]);
  const [TotalPage, setPageNation] = useState([]);
  const [page, setPage] = useState(1);

  const handlePage = (event) => {
    const nowPageInt = parseInt(event.target.outerText);
    setPage(nowPageInt);
  };

  function fetchChallengeListSuccess(res) {
    setChallengeList(res.data);
  }

  function fetchChallengeListFail(err) {
    setChallengeList([]);
  }

  useEffect(() => {
    fetchChallengeList(page, fetchChallengeListSuccess, fetchChallengeListFail);
  }, [page]);

  function fetchChallengePageCntSuccess(res) {
    setPageNation(res.data);
  }

  function fetchChallengePageCntFail(res) {
    setPageNation([]);
  }

  useEffect(() => {
    fetchChallengePageCnt(
      fetchChallengePageCntSuccess,
      fetchChallengePageCntFail
    );
  }, []);

  return ChallengeList.length === 0 ? (
    <MainBox>
      <Text> 생성된 챌린지가 없어요 </Text>
    </MainBox>
  ) : (
    <MainBox>
      <WatchToggleBox>
        <WatchToggle />
      </WatchToggleBox>

      <ChallengeListBox>
        {/* 받아온 챌린지리스트 순회하기 */}
        {ChallengeList.map((item) => {
          return (
            <ChallengeCard
              key={item.chlId}
              chlId={item.chlId}
              imgurl={item.imgurl}
              tagList={item.tagList}
              title={item.title}
              contents={item.contents}
              startTime={item.startTime}
              endTime={item.endTime}
              categoryId={item.categoryId}
              alarm={item.alarm}
              watch={item.watch}
            ></ChallengeCard>
          );
        })}
      </ChallengeListBox>

      <ButtonBox>
        <Button size="ss" my="0">
          챌린지 생성
        </Button>
      </ButtonBox>

      <PageBox>
        <Stack spacing={2}>
          <Pagination
            count={TotalPage}
            defaultPage={1}
            shape="rounded"
            onChange={(e) => handlePage(e)}
          />
        </Stack>
      </PageBox>
    </MainBox>
  );
}
