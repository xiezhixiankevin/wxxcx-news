// pages/me/me.js
Page({

    data: {
        contentNewsList: [],
        userBaseInfo:{},
        //是否是收藏页
        isCollect: true,
        isFirst:true
    },

    tapCollect(){
        console.log("[[tapCollect]]");
        this.setData({
            isCollect: true
        })
        this.getContentNewsList();
    },

    tapDianzan(){
        console.log("[[tapDianzan]]");
        this.setData({
            isCollect: false
        })
        this.getContentNewsList();
    },

    //跳转到用户信息页
    tapUserInfo() {
        console.log("[[tapUserInfo]]");
        //将userBaseInfo存到全局变量中
        getApp().globalData.userBaseInfo = this.data.userBaseInfo;
        wx.navigateTo({
          url: "/pages/userInfo/userInfo"
        });
    },

    //跳转到新闻详情页
    viewDetail: function(e) {
        let newsUrl = e.currentTarget.dataset.newsurl || ''
        let newsuniquekey = e.currentTarget.dataset.newsuniquekey || ''
        let newsthumbnail_pic_s = e.currentTarget.dataset.newsthumbnail_pic_s || ''
        let newsdate = e.currentTarget.dataset.newsdate || ''
        let newsTitle = e.currentTarget.dataset.newstitle || ''
        let newsAuthor = e.currentTarget.dataset.newsauthor || ''
        // appkey time photo
        wx.navigateTo({
        url: '../detail/detail?newsUrl=' + newsUrl+'&newsuniquekey=' + newsuniquekey+'&newsthumbnail_pic_s=' + newsthumbnail_pic_s+'&newsdate=' + newsdate+'&newsTitle=' + newsTitle+'&newsAuthor=' + newsAuthor
        })
    },


    onLoad(options) {
        console.log("[[onLoad]]");
        //从后端获取用户信息
        this.getUserBaseInfo();
        //获取contentNewsList
        this.getContentNewsList();
        this.setData({
            isFirst:false
        })
    },
    
    //从后端获取用户信息
    getUserBaseInfo() {
        wx.request({
            url: 'http://localhost:8080/user/getUserInfo',
            method: 'GET',
            header: {
                "Authorization": wx.getStorageSync("token")
              },
            success: (res) => {
                console.log(res.data);
                this.setData({
                    userBaseInfo: res.data.data
                })
            }
        })
    },

    getContentNewsList(){
        if(this.data.isCollect){
            wx.request({
                url: 'http://localhost:8080/collection/getCollections',
                method: 'GET',
                header: {
                    "Authorization": wx.getStorageSync("token")
                },
                success: (res) => {
                    console.log(res.data);
                    this.setData({
                        contentNewsList: res.data.data
                    })
                }
            })
        }else{
            wx.request({
                url: 'http://localhost:8080/likes/getLikes',
                method: 'GET',
                header: {
                    "Authorization": wx.getStorageSync("token")
                },
                success: (res) => {
                    console.log(res.data);
                    this.setData({
                        contentNewsList: res.data.data
                    })
                }
            })
        }
    },

    onShow() {
        console.log("[[onShow]]")
        if(!this.data.isFirst){
            this.getUserBaseInfo();
            this.getContentNewsList();
        }
    }

})