import React, { useState } from "react";

const Search = ({getQuery}) => {
  const [text, setText] = useState("");

  const onChange = (e) => {
      setText(e)
  }
  const search = (e) => {
    e.preventDefault();
    getQuery(text);
  }

  return (
    <section className="search">
      <form className="form-control">
        <input
          type="text"
          placeholder="Search city"
          value={text}
          onChange={(e) => onChange(e.target.value)}
          autoFocus
        />
        <button className="btn" onClick={(e)=>search(e)}>Search</button>
      </form>
      
    </section>
  );
};

export default Search;
