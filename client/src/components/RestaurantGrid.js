import React from "react"
import RestaurantItem from "./RestaurantItem"


const RestaurantGrid = ({ restaurants, setId, voted, votedRestaurant}) => {
    return (
        <section className="cards">
            {restaurants.map(restaurant => (
                    <RestaurantItem key={restaurant.id} restaurant={restaurant} setRestaurantId={setId} voted={voted} votedRestaurant={votedRestaurant}/>
                
            ))}
        </section>
    )
}

export default RestaurantGrid