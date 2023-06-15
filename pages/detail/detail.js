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
      nickname: '匿名用户',
      content: content,
      url: this.data.url
    }

    // 添加评论
    let this1 = this;
    wx.request({
        url: 'http://localhost:8080/comment/addNewComment',
        method: 'post',
        data: {
            url: this1.data.newsUrl,
            content: newComment.content
        },
        header: {
            'content-type': 'application/x-www-form-urlencoded',
            "Authorization": wx.getStorageSync("token")
        },
        success(res) {
            // 获取新评论
            wx.request({
                url: 'http://localhost:8080/news/getNews',
                method: 'post',
                data: {
                    url:this1.data.newsUrl,
                    title:this1.data.newsTitle,
                    author_name:this1.data.newsAuthor,
                    date:this1.data.newsdate,
                    thumbnail_pic_s:this1.data.newsthumbnail_pic_s
                },
                header: {
                    'content-type': 'application/x-www-form-urlencoded',
                    "Authorization": wx.getStorageSync("token")
                },
                success(res) {
                    this1.setData({
                        comments: res.data.data.comments,
                        commentCount: res.data.data.comments==null?0:res.data.data.comments.length,
                        likeNum:res.data.data.news.likes,
                        collectNum:res.data.data.news.collections,
                    });
                },
                fail(err) {
                  console.error(err)
                }
              });
        },
        fail(err) {
          console.error(err)
        }
      });


    this.setData({
    //   comments: this.data.comments.concat(newComment),
    //   commentCount: this.data.commentCount + 1,
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
      let this1 = this;
      wx.request({
        url: 'http://localhost:8080/likes/kudos',
        method: 'post',
        data: {
            url: this1.data.newsUrl,
            type: that?1:-1,
        },
        header: {
            'content-type': 'application/x-www-form-urlencoded',
            "Authorization": wx.getStorageSync("token")
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
      let this1 = this;
      wx.request({
        url: 'http://localhost:8080/collection/collect',
        method: 'post',
        data: {
            url: this1.data.newsUrl,
            type: that?1:-1,
        },
        header: {
            'content-type': 'application/x-www-form-urlencoded',
            "Authorization": wx.getStorageSync("token")
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
        url: 'http://localhost:8080/news/getNews',
        method: 'post',
        data: {
            url:newsUrl,
            title:that.data.newsTitle,
            author_name:that.data.newsAuthor,
            date:that.data.newsdate,
            thumbnail_pic_s:that.data.newsthumbnail_pic_s
        },
        header: {
            'content-type': 'application/x-www-form-urlencoded',
            "Authorization": wx.getStorageSync("token")
        },
        success(res) {
            that.setData({
                comments: res.data.data.comments,
                commentCount: res.data.data.comments==null?0:res.data.data.comments.length,
                likeNum:res.data.data.news.likes==null?0:res.data.data.news.likes,
                collectNum:res.data.data.news.collections==null?0:res.data.data.news.collections,
            });
        },
        fail(err) {
          console.error(err)
        }
      });
      let this1 = this;
    // 获取是否自己点过
    wx.request({
        url: 'http://localhost:8080/likes/ifLiked',
        method: 'post',
        data: {
            url: this1.data.newsUrl,
        },
        header: {
            'content-type': 'application/x-www-form-urlencoded',
            "Authorization": wx.getStorageSync("token")
        },
        success(res) {
            that.setData({
                likeControl: !res.data.data
            });
        },
        fail(err) {
          console.error(err)
        }
      });
      // 获取是否自己收藏过
      wx.request({
          url: 'http://localhost:8080/collection/ifCollected',
          method: 'post',
          data: {
              url: this1.data.newsUrl,
          },
          header: {
              'content-type': 'application/x-www-form-urlencoded',
              "Authorization": wx.getStorageSync("token")
          },
          success(res) {
              that.setData({
                  collectControl: !res.data.data
              });
          },
          fail(err) {
            console.error(err)
          }
        });
    
    
    
    
    
  }
})
