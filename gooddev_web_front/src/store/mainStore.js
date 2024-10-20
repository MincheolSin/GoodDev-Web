import api from '@/api/api';
import { defineStore } from 'pinia';
import { computed, reactive, watch } from 'vue';

export const useMainStore = defineStore('mainStore',()=>{
    
    const initialLoginInfo = {
        mid: '', // 아이디
        password: '', // 비밀번호
        member_name: '', // 회원명(실명)
        nickname: '', // 닉네임
        phone: '', // 전화번호
        email: '', // 이메일
        auto_Login: '', // 자동로그인 key
        isAdminYn: '', // 관리자 여부
        signup_Date: '', // 가입날짜
        class_No: '',
    };
    
    const state = reactive({
        searchKeyword : '',

        searchData : {},

        loginInfo : { ...initialLoginInfo },

        pageRequestDTO:{
            page : 1,
            size : 10,
            keyword: '',
            category_no: '',
        },
        pageResponseDTO : {
            size : '',
            total : '',
            start : '',
            end : '',
            prev : '',
            next : '',
            list : [],
        },
        totalCategory : {},
    });

    const doLogin = async (loginInfo)=>{  
        try{
            const response = await api.getLogin(loginInfo);
            if(response.data.loginInfo != null){
                state.loginInfo = response.data.loginInfo;
                return true;
            }
            else return false;
        }
        catch (error) {
            console.error("API 요청 중 오류 발생:", error);
        }
    };

    const doLogout = () =>{
        state.loginInfo = initialLoginInfo;
    };

    const postMember = async (memberDTO) =>{
        try{
            const response = await api.postMember(memberDTO);
            return response.data;
        }
        catch (error) {
        console.error("API 요청 중 오류 발생:", error);
        }
    }
    
    const idCheck = async (mid) =>{
        try{
            const response = await api.getDuplicateID(mid);
            return response.data;
        }
        catch (error) {
            console.error("API 요청 중 오류 발생:", error);
        }
    }

    const emailCheck = async (email) =>{
        try{
            const response = await api.getDuplicateEmail(email);
            return response.data;
        }
        catch (error) {
            console.error("API 요청 중 오류 발생:", error);
        }
    }
    const getMainPage = async ()=>{
        try{
            const response = await api.getMainPage();
            return response.data;
        }
        catch (error) {
            console.error("API 요청 중 오류 발생:", error);
        }
    };
      
    const getPageResponseDTO = async () =>  {
          try{
              const response = await api.getList(state.pageRequestDTO.page,state.pageRequestDTO.size,state.pageRequestDTO.category_no,state.pageRequestDTO.keyword);
              state.pageResponseDTO = response.data.pageResponseDTO;
              state.totalCategory = response.data.totalCategory;
          }
          catch (error) {
              console.error("API 요청 중 오류 발생:", error);
           }
    };

    const updatePageRequestDTO = ({
        page,
        size,
        category_no,
        keyword,
      } = {}) => {
        state.pageRequestDTO = {
          ...state.pageRequestDTO,
          ...(page !== undefined && { page }),
          ...(size !== undefined && { size }),
          ...(keyword !== undefined && { keyword }),
          ...(category_no !== undefined && { category_no }),
        };
        getPageResponseDTO();
      };

    const getSearch = async () =>{
        try{
            const response = await api.getSearch(state.searchKeyword);
            state.searchData = response.data.totalMap;
        }
        catch (error) {
            console.error("API 요청 중 오류 발생:", error);
        }
    }
    
    const updateSearchKeyword = (keyword)=>{
        state.searchKeyword = keyword;
    }

    const getBoard = async (bno) =>  {
        try{
            const response = await api.getRead(bno);
            return response.data
        }
        catch (error) {
            console.error("API 요청 중 오류 발생:", error);
        }
      };

    const postBoard = async(boardDTO) =>{
        try{
            const response = await api.postBoard(boardDTO);
            return response.data;
        }catch(error){
            console.error("API 요청 중 오류 발생:", error);
            return false;
        }
    }

    const updateBoard = async(boardDTO) =>{
        try{
            const response = await api.updateBoard(boardDTO);
            return response.data;
        }catch(error){
            console.error("API 요청 중 오류 발생:", error);
            return false;
        }
    }

    const deleteBoard = async(bno,board_password) =>{
        try{
            const response = await api.deleteBoard(bno,board_password);
            return response.data;
        }catch(error){
            console.error("API 요청 중 오류 발생:", error);
            return false;
        }
    }


    const loginInfo= computed(()=>state.loginInfo);
    const pageRequestDTO= computed(()=>state.pageRequestDTO);
    const pageResponseDTO= computed(()=>state.pageResponseDTO);
    const totalCategory = computed(()=> state.totalCategory);
    const searchKeyword = computed(()=> state.searchKeyword);
    const searchData = computed(()=>state.searchData);

    const saveLoginInfo = () => {
        localStorage.setItem('loginInfo', JSON.stringify(state.loginInfo));
      };

    const loadLoginInfo = () => {
    const savedLoginInfo = localStorage.getItem('loginInfo');
    console.log(savedLoginInfo)
        if (savedLoginInfo) {
            Object.assign(state.loginInfo, JSON.parse(savedLoginInfo));
        }
    };
    
    loadLoginInfo();

    watch(
        () => state.loginInfo,
        () => {
          saveLoginInfo();
        },
        { deep: true }
      );

    return {
        loginInfo,
        pageRequestDTO,
        pageResponseDTO,
        totalCategory,
        searchKeyword,
        searchData,
        doLogin,
        doLogout,
        idCheck,
        emailCheck,
        postMember,
        updatePageRequestDTO,
        getPageResponseDTO,
        getMainPage,
        postBoard,
        getBoard,
        updateBoard,
        deleteBoard,
        getSearch,
        updateSearchKeyword
      }
    }, {
});