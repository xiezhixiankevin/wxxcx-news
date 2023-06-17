const app = getApp()
// const appKey = '92d1b194fde6d11ed4c36f07c6def57d' // 用于访问新闻接口的appKey xzx
// const appKey = 'e8c298f8cb626f763df2aeefd590bc18' // 用于访问新闻接口的appKey zcx
const appKey = '7341610d45c4f3e0228f4ed302df9b3d' // 用于访问新闻接口的appKey wj
const request = require('../../utils/request.js')
const extractArticleInfo = require('./utils/getArticleTime.js')
const shuffle = require('./utils/shuffle.js')

Page({
  data: {
    headerTitleName: [
      { name: '头条', nameID: '201701', newsType: 'top' },
      { name: '军事', nameID: '201702', newsType: 'junshi' },
      { name: '体育', nameID: '201703', newsType: 'tiyu' },
      { name: '科技', nameID: '201704', newsType: 'keji' },
      { name: '财经', nameID: '201705', newsType: 'caijing' },
      { name: '社会', nameID: '201706', newsType: 'shehui' },
      { name: '时尚', nameID: '201707', newsType: 'shishang' },
      { name: '娱乐', nameID: '201708', newsType: 'yule' },
      { name: '国内', nameID: '201709', newsType: 'guonei' },
      { name: '国际', nameID: '2017010', newsType: 'guoji' }
    ],
    newsType:"top",
    flag: true, //触底开关
    page:1,
    swiperIndex: '1/4',
    topPic: [],
    tapID: 201701, // 判断是否选中
    contentNewsList: [],
    showCopyright: false,
    refreshing: false,
    subtitle:'纵览天下'
  },
  scrollToLower:function(){
    wx.showLoading({
        title: '加载中..'
        });
    this.data.page += 1;
    this.updated(this.data.newsType,this.data.page);
  },
  onLoad: function() {
    this.renderPage('top', false, () => {
      this.setData({
        showCopyright: true
      })
    });
    this.data.page = 2;
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh:function(){
    this.onRefresh();
  },

   // 处理触底数组合并
   updated(newsType,page) {
    var that = this;
    request({ url: `https://v.juhe.cn/toutiao/index?type=${newsType}&key=${appKey}&page=${page}`, newstype: newsType })
    .then(res => {
        wx.hideLoading()
        let { articleList, topPic } = extractArticleInfo(res.result.data);
        let articleList1 = that.data.contentNewsList.concat(articleList);
        console.log(articleList1.length)
        this.setData({
            contentNewsList: articleList1
        });
    })
    .catch(error => {
        wx.hideLoading()
    });
  },


  onRefresh:function(){
    //导航条加载动画
    wx.showNavigationBarLoading()
    //loading 提示框
    wx.showLoading({
      title: 'Loading...',
    })
    console.log("下拉刷新啦");
    var that = this;
    setTimeout(function () {
      wx.hideLoading();
      wx.hideNavigationBarLoading();
      that.renderPage('top', false, () => {
        that.setData({
          refreshing: false
        })
      })
      that.data.page = 2;
      //停止下拉刷新
      wx.stopPullDownRefresh();
    }, 2000)
  },


  // headerBar 点击
  headerTitleClick: function(e) {
    this.setData({ tapID: e.target.dataset.id })
    this.data.newsType = e.currentTarget.dataset.newstype;
    this.renderPage(e.currentTarget.dataset.newstype, false)
  },

  //跳转到新闻详情页
  viewDetail: function(e) {
    console.log( e.currentTarget.dataset.newsauthor.substring(0,6))
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

  handleSwiperChange: function(e) {
    this.setData({
      swiperIndex: `${e.detail.current + 1}/4`
    })
  },

  // isRefresh 是否为下拉刷新
  renderPage: function(newsType, isRefresh, calllBack) {
      wx.showLoading({
        title: '加载中'
      })
      var that = this;
      request({ url: `https://v.juhe.cn/toutiao/index?type=${newsType}&key=${appKey}`, newstype: newsType })
        .then(res => {
          wx.hideLoading()
          let { articleList, topPic } = extractArticleInfo(res.result.data)
          this.setData({
            contentNewsList: articleList,
            topPic:topPic
          });
          if (calllBack) {
            calllBack()
          }
        })
        .catch(error => {
          wx.hideLoading()
        })
  }
})
