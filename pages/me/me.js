// pages/me/me.js
Page({

    data: {
        contentNewsList: [],
        userBaseInfo:{},
        //是否是收藏页
        isCollect: true,
    },

    //跳转到用户信息页
    tapUserInfo() {
        console.log("[[tapUserInfo]]");
        // getApp().globalData.userBaseInfo = this.userBaseInfo;
        wx.navigateTo({
          url: "/pages/userInfo/userInfo"
        });
    },

    //跳转到新闻详情页
    viewDetail: function(e) {
        let newsUrl = e.currentTarget.dataset.newsurl || ''
        let newsTitle = e.currentTarget.dataset.newstitle || ''
        let newsAuthor = e.currentTarget.dataset.newsauthor || ''
        wx.navigateTo({
        url: '../detail/detail?newsUrl=' + newsUrl
        })
    },


    onLoad(options) {
        console.log("[[onLoad]]");
        //从后端获取用户信息
        // this.getUserBaseInfo();
        //获取contentNewsList
        this.getContentNewsList();
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
    },

    onShow() {

    }

})