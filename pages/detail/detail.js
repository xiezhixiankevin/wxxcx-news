// pages/detail/detail.js
const request = require('../../utils/request.js')
const util = require('../../utils/util.js')
var WxParse = require('../../components/wxParse/wxParse.js')

Page({
  /**
   * 页面的初始数据
   */
  data: {
    newsTitle: '',
    newsUrl: '',
    newsAuthor: '',
    newsuniquekey: '',
    newsthumbnail_pic_s: '',
    newsdate: '',
    likeNum:0,
    likeControl:true,
    collectNum:0,
    collectControl:true,
    url:'',
    contentTip: '由于后台接口原因，新闻具体内容无法编辑，只返回了一个新闻链接...',
    comments:[],
    commentCount:0,
    commentInput:"",
    // 是否可以提交评论
    canSubmit: false
  },

    // 监听评论输入框的输入事件
    inputComment(e) {
        const content = e.detail.value.trim()
        this.setData({
            commentInput: content,
            canSubmit: content !== ''
        })
    },

    // 提交新的评论
  submitComment() {
    const content = this.data.commentInput.trim()
    if (!content) {
      return
    }

    const newComment = {
      name: '匿名用户',
      content: content,
      url: this.data.url
    }

    // 添加评论
    wx.request({
        url: 'http://localhost:8080/comment/addComments',
        method: 'post',
        data: {
            url: newComment.url,
            name: newComment.name,
            content: newComment.content
        },
        header: {
            'content-type': 'application/x-www-form-urlencoded'
        },
        success(res) {
            console.log(res)
        },
        fail(err) {
          console.error(err)
        }
      })

    this.setData({
      comments: this.data.comments.concat(newComment),
      commentCount: this.data.commentCount + 1,
      commentInput: '',
      canSubmit: false
    })

    wx.showToast({
      title: '评论成功！'
    })
  },
  // 喜欢
  likeTap() {
      if(this.data.likeControl){
        this.setData({
            likeNum:this.data.likeNum+1
        });
      }else{
        this.setData({
            likeNum:this.data.likeNum-1
        });
      }
      this.setData({
        likeControl: this.data.likeControl?false:true
      });
      let that = !this.data.likeControl;
      wx.request({
        url: 'http://localhost:8080/news/like',
        method: 'post',
        data: {
            url: newComment.url,
            type: that,
        },
        header: {
            'content-type': 'application/x-www-form-urlencoded'
        },
        success(res) {
            console.log(res)
        },
        fail(err) {
          console.error(err)
        }
      });

  },
  // 收藏
  collectTap() {
    if(this.data.collectControl){
        this.setData({
            collectNum:this.data.collectNum+1
        });
      }else{
        this.setData({
            collectNum:this.data.collectNum-1
        });
      }
      this.setData({
        collectControl: this.data.collectControl?false:true
      });
      let that = !this.data.collectControl;
      wx.request({
        url: 'http://localhost:8080/news/collect',
        method: 'post',
        data: {
            url: newComment.url,
            type: that,
        },
        header: {
            'content-type': 'application/x-www-form-urlencoded'
        },
        success(res) {
            console.log(res)
        },
        fail(err) {
          console.error(err)
        }
      });      
      
},

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let newsUrl = options.newsUrl;
    this.setData({
        newsAuthor:options.newsAuthor,
        newsUrl:options.newsUrl,
        newsTitle:options.newsTitle,
        newsuniquekey:options.newsuniquekey,
        newsthumbnail_pic_s:options.newsthumbnail_pic_s,
        newsdate:options.newsdate,
    });
    let that = this;
    that.setData({
        url: newsUrl
    });
    // 获取新闻
    request({
      url: newsUrl
    }).then(res => {
      WxParse.wxParse('newsDetailData', 'html', util.getBodyHtml(res), this)
    });
    
    // 获取评论，点赞数，收藏数
    wx.request({
        url: 'http://localhost:8080/news/listNewsInfo',
        method: 'post',
        data: {
            url:newsUrl,
            title:that.data.newsTitle,
            author_name:that.data.newsAuthor,
            date:that.data.newsdate,
            thumbnail_pic_s:that.data.newsthumbnail_pic_s
        },
        header: {
            'content-type': 'application/x-www-form-urlencoded'
        },
        success(res) {
            that.setData({
                comments: res.data.data.list,
                commentCount: res.data.data.list.length,
                likeNum:res.data.data.likeNum,
                collectNum:res.data.data.collectNum,
            });
        },
        fail(err) {
          console.error(err)
        }
      });
    
    
    
    
    
  }
})
