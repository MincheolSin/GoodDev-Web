import axios from 'axios';

const apiClient = axios.create({
  withCredentials: false,
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json'
  }
});

export default {
  getList(page,size,category_no,keyword) {
    const url = "/api/boardlist/?page="+page+"&size="+size +"&category_no="+category_no+"&keyword=" +keyword;
    return apiClient.get(url);
  },

  getRead(bno) {
    const url = "/api/boardread/"+bno;
    return apiClient.get(url);
  },

  postMember(memberDTO){
    const url = "/api/member/register";
    return apiClient.post(url,memberDTO);
  },
  
  getLogin(loginInfo){
    const url = "/api/member/login";
    return apiClient.post(url,loginInfo);
  },
  
  postBoard(boardDTO){
    const url = "/api/board/insert";
    return apiClient.post(url,boardDTO);
  },

  getDuplicateID(mid){
    const url = "/api/member/checkIdDuplicate/"+mid;
    return apiClient.get(url);
  },

  getDuplicateEmail(email){
    const url = "/api/member/checkEmailDuplicate/"+email;
    return apiClient.get(url);
  },

  updateBoard(boardDTO){
    const url = "/api/board/update";
    return apiClient.patch(url,boardDTO);
  },

  deleteBoard(bno,board_password){
    const url = "/api/board/delete/"+bno+"/"+board_password;
    return apiClient.delete(url);
  },

  getMainPage(){
    const url = "/api/main";
    return apiClient.get(url);
  },
  getSearch(keyword){
    const url = "/api/search/"
    return apiClient.get('/api/search', {
      params: {
        keyword: keyword
      }
    });
  }
};