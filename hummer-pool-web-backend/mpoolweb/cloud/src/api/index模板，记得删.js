//引入模块使用
import axios from 'axios'
axios.defaults.baseURL = 'http://lovegf.cn:8899';

//暴露出去
//获取轮播图
export const getLunbotu =()=>{
    return axios('/api/getlunbo')
}

//获取新闻列表数据
export const getNewList= ()=>{
    return axios('/api/getnewslist')
}
//newinfo页面数据显示
export const getInfo = ({aa:id})=>{
    return axios('/api/getnew/'+id)
}
//加载更多
export const getcomments = (params)=>{
    // /api/getcomments/:artid?pageindex=1
    return axios(`/api/getcomments/${params.id}?pageindex=${params.pageindex}`)
}
//发表评论 /api/postcomment/:artid 
export const postcomment = (params)=>{ //{artid:this.id,content:this.msg}
    return axios.post(`/api/postcomment/${params.artid}`,{content:params.content})
}


//图片页标题数据 /api/getimgcategory
export const getimgcategory = ()=>{ 
    return axios(`/api/getimgcategory`)
}

//根据id获取对应图片列表
export const getimages = (cateid)=>{ ///api/getimages/:cateid
    return axios(`/api/getimages/${cateid}`)
}

//imgInfo获取图片详细数据
export const getimageInfo = (imgid)=>{///api/getimageInfo/:imgid
    return axios('/api/getimageInfo/'+imgid)
}

//缩略图
export const getThumImages = (imgid)=>{///api/getthumimages/:imgid
    return axios('/api/getthumimages/'+imgid)
}
//商品列表
export const getgoods = (number)=>{///api/getthumimages/:imgid
    return axios(`/api/getgoods?pageindex=${number}`)
}


//图文详情
export const getdesc = (id)=>{///api/goods/getdesc/:id
    return axios(`/api/goods/getdesc/${id}`)
}
//商品详情
export const getinfo = (id)=>{///api/goods/getinfo/:id
    return axios(`/api/goods/getinfo/${id}`)
}
/* 获取购物车列表数据 */
export const  getGoodsList= params => {
    return axios.get(`/api/goods/getshopcarlist/${params}`).then(res=>res)
};