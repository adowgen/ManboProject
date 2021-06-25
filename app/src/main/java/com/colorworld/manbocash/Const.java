package com.colorworld.manbocash;/*
*
* => roodData로 저장 필요 20.09.18
* getSharedPreferences("stepCount", MODE_PRIVATE);
*
* key : reset (boolean) ------------- 자정이후 리셋, 자정때 서버 통신하면 false 로 초기화 ----- deprecate
*     : appOsCount (int) ------------ 앱과는 상관없는 step_sensor 자체의 데이터, 최초 설치시 및 자정 이후 리셋 때 걸음수를 0를 만들기 위함
*     : appOsCount_Background (int) - 자정에 리셋하면서 os 카운트를 저장해야하기 때문에 stepService에서 최신 데이터 저장
*     : convertedCount (int) -------- 코인으로 전환한 워크 카운트
*     : heartCoinCount (int) -------- 하트에 클릭 가능한 코인 카운트
*     : myCash (long) --------------- 나의 캐쉬
*     : rewardCash (long) ----------- 리워드광고 판단 캐쉬
*     : compareDate (string) -------- 날짜가 다르면 서버에 스탭수 저장
*     : compareTime (string) -------- 시간이 다르면 디비에 스탭수 저장
*     : googleReward (int) --------- 구글 리워드 광고 판단 포인트
*
*
*
*
*
*
* getSharedPreferences("manboData", MODE_PRIVATE);
*
* key : greeting (string) ------------------ 메인 인사 텍스트
*     : firstInstall (boolean) ------------- 최초실행
*     : profileImagePath (String) ---------- 프로필 이미지 패스
*
*
* */


public class Const {

    public static class lottieImageType {
        public static final int DOGWITHMAN = 1;
        public static final int WALKINGDOG = 2;
        public static final int COUPLE = 3;
        public static final int DOGWITHWOMAN = 4;
        public static final int DEADPOOL = 5;
        public static final int REDDRESS = 6;
        public static final int ROBOT = 7;
    }

    public static class LoginType {
        public static final int PASSWORD = 0;
        public static final int KAKAO = 1;
        public static final int FACEBOOK = 2;
        public static final int GOOGLE = 3;
    }

    public static class TermsType {
        public static final int PRIVACY = 0;
        public static final int LOCATION = 1;
        public static final int PUSH = 2;
    }

    public static class TermsURL {
        public static final String PRIVACY_URL = "http://mobile.barosvc.com/info/management";
        public static final String LOCATION_URL = "http://mobile.barosvc.com/info/management";
        public static final String PUSH_URL = "http://mobile.barosvc.com/info/management";
    }

    public static class RegisteredBy {
        public static final String MANBOCASH = "manbocash";
        public static final String KAKAO = "kakao";
        public static final String FACEBOOK = "facebook";
        public static final String GOOGLE = "google";
    }

}

