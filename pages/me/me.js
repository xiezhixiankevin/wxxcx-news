// pages/me/me.js
Page({

    data: {
        contentNewsList: []
    },

    //跳转到用户信息页
    tapUserInfo() {
        console.log("[[tapUserInfo]]");
        getApp().globalData.userBaseInfo = this.userBaseInfo;
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

    },
 

    onShow() {

    }

})