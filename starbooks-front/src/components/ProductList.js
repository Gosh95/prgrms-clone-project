import React from "react";
import {Product} from "./Product";

export function ProductList({products = [], onAddClick, onMinusClick}) { //기본 값을 설정할 수 있다.
    return (
        <React.Fragment>
            <h5 className="flex-grow-0"><b>상품 목록</b></h5>
            <ul className="list-group products">
                {products.map(v =>
                    <li key={v.productId} className="list-group-item d-flex mt-3">
                        <Product {...v} onAddClick={onAddClick} onMinusClick={onMinusClick}/>
                    </li>
                )}
            </ul>
        </React.Fragment>
    )
}