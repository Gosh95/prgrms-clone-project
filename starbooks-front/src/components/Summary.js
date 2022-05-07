import {SummaryItem} from "./SummaryItem";
import React, {useState} from "react";

export function Summary({items = [], onOrderSubmit}) {
    const totalPrice = items.reduce((prev, current) => prev + (current.quantity * current.price), 0);
    const [order, setOrder] = useState({
        customerId: "", paymentMethod: "CASH", discountPolicy: "NO_CHOICE"
    });

    const handleCustomerIdInputChanged = (e) => setOrder({...order, customerId: e.target.value});

    const handlePaymentMethodInputChanged = (e) => setOrder({...order, paymentMethod: e.target.value});

    const handleDiscountPolicyInputChanged = (e) => setOrder({...order, discountPolicy: e.target.value});

    const handleSubmit = (e) => {
        if (order.customerId === "" || order.paymentMethod === "" || order.discountPolicy === "") {
            alert("입력값을 확인해주세요!");
        } else {
            onOrderSubmit(order);
        }
    }

    const discountPrice = () => {
        if (order.discountPolicy === "FIXED") {
            if (totalPrice < 2000) {
                return 0;
            }

            return 2000;
        } else if(order.discountPolicy === "PERCENT") {
            return Math.round(totalPrice * 0.1);
        }

        return 0;
    }

    const discountedTotalPrice = () => {
        if (order.discountPolicy === "FIXED") {
            if (totalPrice < 2000) {
                return totalPrice;
            }
            return totalPrice - 2000;
        } else if(order.discountPolicy === "PERCENT") {
            return totalPrice - Math.round(totalPrice * 0.1);
        }

        return totalPrice;
    }

    return (
        <>
            <div>
                <h5 className="m-0 p-0"><b>Summary</b></h5>
            </div>
            <hr/>
            {items.map(v => <SummaryItem key={v.productId} quantity={v.quantity} productName={v.productName}/>)}
            <form>
                <div className="mb-3">
                    <label htmlFor="customerId" className="form-label">회원 아이디</label>
                    <input type="text" className="form-control mb-1" value={order.customerId} onChange={handleCustomerIdInputChanged} id="customerId"/>
                </div>
                <div className="mb-3">
                    <label htmlFor="paymentMethod" className="form-label">결제 수단</label>
                    <select onChange={handlePaymentMethodInputChanged} id="paymentMethod" className="form-select">
                        <option value={"CASH"}>
                            CASH
                        </option>
                        <option value={"CARD"}>
                            CARD
                        </option>
                    </select>
                </div>
                <div className="mb-3">
                    <label htmlFor="discountPolicy" className="form-label">할인 정책</label>
                    <select onChange={handleDiscountPolicyInputChanged} id="discountPolicy" className="form-select">
                        <option value={"NO_CHOICE"}>
                            NO_CHOICE
                        </option>
                        <option value={"FIXED"}>
                            FIXED
                        </option>
                        <option value={"PERCENT"}>
                            PERCENT
                        </option>
                    </select>
                </div>
                <div>당일 오후 2시 이후의 주문은 다음날 배송을 시작합니다.</div>
            </form>
            <div className="row pt-2 pb-2 border-top">
                <h5 className="col">상품금액</h5>
                <h5 className="col text-end">{totalPrice}원</h5>
            </div>
            <div className="row pt-2 pb-2 border-top">
                <h5 className="col">할인금액</h5>
                <h5 className="col text-end">{-discountPrice(totalPrice)}원</h5>
            </div>
            <div className="row pt-2 pb-2 border-top">
                <h5 className="col">결제금액</h5>
                <h5 className="col text-end">{discountedTotalPrice(totalPrice)}원</h5>
            </div>
            <button className="btn btn-dark col-12" onClick={handleSubmit}>결제하기</button>
        </>
    )
}