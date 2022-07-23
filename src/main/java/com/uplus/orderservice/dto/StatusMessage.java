package com.uplus.orderservice.dto;

public class StatusMessage {
    public static final String NOT_AVAILABLE_SERVICE="서비스를 사용할 수 없습니다.";

    public static final String NOT_FOUND_CUSTOMER = "주문자 이름 또는 전화번호를 찾을 수 없습니다.";
    public static final String SUCCESS_READ_CUSTOMER = "주문자 조회에 성공하였습니다.";

    public static final String NOT_FOUND_ORDER_NUMBER = "알맞은 주문 번호를 찾을 수 없습니다.";
    public static final String SUCCESS_READ_PRODUCT_ORDER = "주문 조회에 성공하였습니다.";

    public static final String NOT_FOUND_PRODUCT_ORDER = "상품 정보를 확인할 수 없습니다.";
    public static final String NOT_MATCH_PRODUCT_ORDER_PRICE = "주문 정보가 다릅니다. 주문을 다시 진행해주세요.";

    public static final String SUCCESS_PAY_PRODUCT_ORDER = "주문 결제가 완료되었습니다.";
    public static final String FAIL_PRODUCT_ORDER = "주문 결제가 실패하였습니다.";

}
