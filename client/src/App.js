
import React, { useState, useEffect } from 'react'
import axios from 'axios'
import './App.css';
import Header from './components/Header'
import RestaurantGrid from './components/RestaurantGrid'
import Search from './components/Search';

const App = () => {
  const [restaurants, setRestaurants] = useState([])
  const [city, setCity] = useState("")
  const [restaurantId, setRestaurantId] = useState("");
  const [voted, setVoted] = useState(null);
  const [votedRestaurant, setVotedRestaurant] = useState("");
  const [runner, setRunner] = useState(0);

  // Fetching restaurants with requested city
  useEffect(() => {
    const fetchRestaurants = async () => {
      console.log("runner:", runner)
      const result = await axios(`/api/v1/restaurants/${city}`)
      
      console.log(result)
      setRestaurants(result.data.restaurants)
      setVoted(result.data.alreadyVoted)
    }
    if (city !== "") fetchRestaurants()
  }, [city, runner])

  // Posting vote
  useEffect(() => {
    const postVote = async () => {
      const result = await axios.post(`/api/v1/vote/${restaurantId}`)
      
      console.log(result);
      setVotedRestaurant(restaurantId);
      setRestaurantId("");
      setRunner(()=>runner + 1);
    }

    if (restaurantId !== "") postVote();
  }, [restaurantId, runner])

  return (
    <div className="container">
      <Header />
      <Search getQuery={(e)=>setCity(e)}/>
      <RestaurantGrid restaurants={restaurants} setId={setRestaurantId} voted={voted} votedRestaurant={votedRestaurant}/>
    </div>
  );
}

export default App;
