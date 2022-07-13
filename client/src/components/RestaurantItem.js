import React from "react";

const RestaurantItem = ({ restaurant, setRestaurantId, voted, votedRestaurant }) => {


  return (
    <div className="card">
      <div className="card-flipper">
        <div className="card-inner">
          <div className="card-front">
            <h1>{restaurant.name}</h1>
            <ul>
              <li>
                <strong>Opening hours: </strong> {restaurant.openingHours}
              </li>
              <li>
                <strong>Votes: </strong> {restaurant.votes}
              </li>
            </ul>
          </div>
          <div className="card-back">
            <h1>Menu</h1>
            <ul>
              {restaurant.dishes.map((dish, index) => (
                <li key={index}>
                  <strong>{dish.name !== "" ? "Option: " + (index + 1) : ""}</strong> {dish.name} {dish.price}
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>
      <button className="card-btn" onClick={()=>{setRestaurantId(restaurant.id)}}>
        {voted ? votedRestaurant === restaurant.id ? "DELETE VOTE" : "CHANGE VOTE" : "VOTE"}
        </button>
    </div>
  );
};

export default RestaurantItem;