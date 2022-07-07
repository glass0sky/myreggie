function loginApi(data) {
  //通过$axios向后端发起Ajax请求
  return $axios({
    'url': '/employee/login',  //请求地址
    'method': 'post',  //请求方式
    data   //请求携带数据
  })
}

function logoutApi(){
  return $axios({
    'url': '/employee/logout',
    'method': 'post',
  })
}
