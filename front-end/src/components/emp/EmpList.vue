<template>
  <!-- EmpList.vue -->
  <div class="list row">
    <!-- 검색어 시작 -->
    <div class="col-md-8">
      <div class="input-group mb-3">
        <input
          type="text"
          class="form-control"
          placeholder="Search by ename"
          v-model="ename"
        />
        <div class="input-group-append">
          <button
            class="btn btn-outline-secondary"
            type="button"
            @click="searchEname"
          >
            Search
          </button>
        </div>
      </div>
    </div>
    <!-- 검색어 끝 -->

    <!-- 전체 목록 조회 시작 -->
    <div class="col-md-6">
      <h4>Emp List</h4>
      <ul class="list-group">
        <li
          class="list-group-item"
          :class="{ active: index == currentIndex }"
          v-for="(data, index) in emp"
          :key="index"
          @click="setActiveEmp(data, index)"
        >
          {{ data.ename }}
        </li>
      </ul>

      <button class="m-3 btn btn-sm btn-danger" @click="removeAllEmp">
        Remove All
      </button>
    </div>
    <!-- 전체 목록 조회 끝 -->

    <!-- 간략 상세 목록 시작 -->
    <div class="col-md-6">
      <div v-if="currentEmp">
        <h4>Emp</h4>
        <div>
          <label><strong>Ename:</strong></label> {{ currentEmp.ename }}
        </div>
        <div>
          <label><strong>job:</strong></label> {{ currentEmp.job }}
        </div>
        <div>
          <label><strong>manager:</strong></label> {{ currentEmp.manager }}
        </div>
        <div>
          <label><strong>hiredate:</strong></label> {{ currentEmp.hiredate }}
        </div>
        <div>
          <label><strong>salary:</strong></label> {{ currentEmp.salary }}
        </div>
        <div>
          <label><strong>commission:</strong></label>
          {{ currentEmp.commission }}
        </div>
        <div>
          <label><strong>eno:</strong></label> {{ currentEmp.eno }}
        </div>

        <router-link :to="'/emp/' + currentEmp.eno"
          ><span class="badge bg-success">Edit</span></router-link
        >
      </div>
      <div v-else>
        <br />
        <p>Please click on a Emp...</p>
      </div>
    </div>
    <!-- 간략 상세 목록 끝 -->
  </div>
</template>

<script>
// axios 공통함수 import
import EmpDataService from "../../services/EmpDataService";

export default {
  // 변수 정의하는 곳 : data(), 초기화
  data() {
    return {
      emp: [],
      currentEmp: null,
      currentIndex: -1,
      ename: "",
    };
  },
  // 함수 정의하는 곳 : methods:
  methods: {
    // axios , 모든 부서 정보 조회 요청 함수
    retrieveEmp() {
      EmpDataService.getAll()
        // 성공하면 .then() 결과가 전송됨
        .then((response) => {
          this.emp = response.data; // springboot 의 전송된 부서 정보
          // 디버깅 콘솔에 정보 출력
          console.log(response.data);
        })
        // 실패하면 .catch() 에 에러가 전송됨
        .catch((e) => {
          console.log(e);
        });
    },
    // 목록을 클릭했을때 현재 부서객체, 인덱스번호를 저장하는 함수
    setActiveEmp(data, index) {
      this.currentEmp = data;
      this.currentIndex = index;
    },
    // axios, 모든 부서 정보 삭제 요청 함수
    removeAllEmp() {
      EmpDataService.deleteAll()
        // 성공하면 .then() 결과가 전송됨
        .then((response) => {
          // 디버깅 콘솔에 정보 출력
          console.log(response.data);
          // 전체 목록 재조회
          this.retrieveEmp();
          // currentEmp, currentIndex 초기화
          this.currentEmp = null;
          this.currentIndex = -1;
        })
        // 실패하면 .catch() 에 에러가 전송됨
        .catch((e) => {
          console.log(e);
        });
    },
    // ename으로 like 검색하는 함수
    searchEname() {
      
      EmpDataService.findByEname(this.ename)
        // 성공하면 .then() 결과가 전송됨
        .then((response) => {
          // 디버깅 콘솔에 정보 출력
          this.emp = response.data; // like 검색 결과를 변수에 저장
          console.log(response.data);
        })
        // 실패하면 .catch() 에 에러가 전송됨
        .catch((e) => {
          console.log(e);
        });
    },
  },
  // 화면이 뜨자마자 실행되는 이벤트(라이프 사이클 함수) : mounted(), created()
  mounted() {
    this.retrieveEmp(); // 화면 로딩시 전체 조회함수 실행
  },
};
</script>

<style>
.list {
  text-align: left;
  max-width: 750px;
  margin: auto;
}
</style>
