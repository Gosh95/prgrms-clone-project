import './App.css';
import 'bootstrap/dist/css/bootstrap.css'
import React, {useEffect, useState} from 'react';
import {ProductList} from "./components/ProductList";
import {Summary} from "./components/Summary";
import axios from "axios";

function App() {
  const [products, setProducts] = useState([
    {productId: 'uuid-1', productName: '콜롬비아 커피1', category: '커피빈', price: 5000},
    {productId: 'uuid-2', productName: '콜롬비아 커피1', category: '커피빈', price: 5000},
    {productId: 'uuid-3', productName: '콜롬비아 커피1', category: '커피빈', price: 5000}
  ]);

  const [items, setItems] = useState([]);

  const handleAddClicked = productId => {
    const product = products.find(v => v.productId === productId);
    const found = items.find(v => v.productId === productId);
    
    const updatedItem =
        found ? items.map(v => v.productId === productId ? {...v, quantity: v.quantity + 1} : v) : [...items, {...product, quantity: 1}];

    setItems(updatedItem);
  }

  const handleMinusClicked = productId => {
    const product = products.find(v => v.productId === productId);
    const found = items.find(v => v.productId === productId);

    const updatedItem =
        found ? items.map(v => v.productId === productId ? {...v, quantity: v.quantity - 1 < 0 ? 0 : v.quantity - 1} : v) : [...items, {...product, quantity: 0}];

    setItems(updatedItem);
  }

  useEffect(() => {
    axios.get('http://localhost:7777/api/v1/products')
        .then(v => setProducts(v.data));
  }, []);

  const handleOrderSubmit = (order) => {
    if (items.length === 0) {
      alert("아이템을 추가해 주세요!");
    } else {
      axios.post('http://localhost:7777/api/v1/orders', {
        customerId: order.customerId,
        paymentMethod: order.paymentMethod,
        discountPolicy: order.discountPolicy,
        orderItems: items.map(v => ({
          productId: v.productId,
          productName: v.productName,
          category: v.category,
          price: v.price,
          quantity: v.quantity
        }))
      }).then(
          v => {
            alert("주문이 정상적으로 접수되었습니다.")

            window.location.href = `http://localhost:7777/${order.customerId}/order-detail`;
          },
          e => {
            const errorMessage = e.response.data.errorMessage;

            if(errorMessage === undefined) {
              alert("서버에러");
            } else {
              alert(errorMessage);
            }

          });
    }
  }

  return (
      <div className="container-fluid">
        <div className="row justify-content-center m-4">
          <h1 className="text-center">STARBOOKS</h1>
        </div>
        <div className="card">
          <div className="row">
            <div className="col-md-8 mt-4 d-flex flex-column align-items-start p-3 pt-0">
              <ProductList products={products} onAddClick={handleAddClicked} onMinusClick={handleMinusClicked}/>
            </div>
            <div className="col-md-4 summary p-4">
              <Summary items={items} onOrderSubmit={handleOrderSubmit}/>
            </div>
          </div>
        </div>
      </div>
  );
}

export default App;
