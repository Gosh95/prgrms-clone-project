import React from "react";

export function SummaryItem({productName, quantity}) {

    return (
        <>
            <div className="row">
                <h6 className="p-0">{productName} <span className="badge bg-dark text-">{quantity}개</span></h6>
            </div>
        </>
    )
}