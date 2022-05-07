import React from "react";

export function Product(props) {
    const productId = props.productId;
    const productName = props.productName;
    const category = props.category;
    const price = props.price;

    const handleAddBtnClicked = e => {
        props.onAddClick(productId);
    }

    const handleMinusBtnClicked = e => {
        props.onMinusClick(productId);
    }

    return (
        <>
            <div className="col-2">
                <img className="img-fluid" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9sYgypcA8MhjHyNJD7-u9L0EjZj7YomKVTA&usqp=CAU" alt=""/>
            </div>
            <div className="col">
                <div className="row text-muted">{category}</div>
                <div className="row">{productName}</div>
            </div>
            <div className="col text-center price">{price}</div>
            <div className="d-grid gap-2 d-md-block">
                <button onClick={handleAddBtnClicked} className="btn btn-small btn-outline-dark">추가</button>
                <button onClick={handleMinusBtnClicked} className="btn btn-small btn-outline-dark">빼기</button>
            </div>
        </>
    )
}