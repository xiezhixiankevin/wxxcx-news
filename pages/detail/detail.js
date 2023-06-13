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
    url:'',
    newsUrl: '',
    newsAuthor: '',
    contentTip: '由于后台接口原因，新闻具体内容无法编辑，只返回了一个新闻链接...',
    comments:[],
    commentCount:0,
    commentInput:"请在此输入评论....",
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
  submitComment(e) {
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

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let { newsUrl } = options;
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
    
    // 获取评论
    wx.request({
        url: 'http://localhost:8080/comment/listComments',
        method: 'get',
        data: {
            url:newsUrl
        },
        header: {
            'content-type': 'application/x-www-form-urlencoded'
        },
        success(res) {
            that.setData({
                comments: res.data.data,
                commentCount: res.data.data.length
            });
        },
        fail(err) {
          console.error(err)
        }
      });
  }
})
