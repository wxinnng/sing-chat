<template>
    <div class="search">
        <input type="text" class="search__input" v-model="searchContent" placeholder="在这查找你的文件吧~">
        <button class="search__button" @click="handleSearch">
            <svg class="search__icon" aria-hidden="true" viewBox="0 0 24 24">
                <g>
                    <path
                        d="M21.53 20.47l-3.66-3.66C19.195 15.24 20 13.214 20 11c0-4.97-4.03-9-9-9s-9 4.03-9 9 4.03 9 9 9c2.215 0 4.24-.804 5.808-2.13l3.66 3.66c.147.146.34.22.53.22s.385-.073.53-.22c.295-.293.295-.767.002-1.06zM3.5 11c0-4.135 3.365-7.5 7.5-7.5s7.5 3.365 7.5 7.5-3.365 7.5-7.5 7.5-7.5-3.365-7.5-7.5z">
                    </path>
                </g>
            </svg>
        </button>
    </div>
</template>

<script setup>
import {inject} from 'vue'

//注入搜索内容（File组件中的内容）
const {searchContent,handleSearch} = inject('searchContent')

</script>

<style scoped>
.search {
  display: flex;
  align-items: center;
  justify-content: space-between;
  text-align: center;
  margin-right: 20px ;
}

.search__input {
  font-family: inherit;
  font-size: inherit;
  background-color: #f4f2f2;
  border: none;
  color: black;
  padding: 0.7rem 1rem;
  border-radius: 30px;
  width: 250px;
  transition: all ease-in-out .5s;
  margin-right: -2rem;
}

.search__input:hover, .search__input:focus {
  box-shadow: 0 0 1em #99595913;
}

.search__input:focus {
  outline: none;
  background-color: #f0eeee;
}

.search__input::-webkit-input-placeholder {
  font-weight: 100;
  color: #111010;
}

.search__input:focus + .search__button {
  background-color: #f0eeee;
}

.search__button {
  border: none;
  background-color: #f4f2f2;
  margin-top: .1em;
}

.search__button:hover {
  cursor: pointer;
}

.search__icon {
  height: 1.3em;
  width: 1.3em;
  fill: #b4b4b4;
}
</style>
