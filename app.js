//app.js
App({
  onLaunch() {
    const logs = wx.getStorageSync("logs") || [];
    logs.unshift(Date.now());
    wx.setStorageSync("logss", logs);
    wx.login({
      success: (res) => {
        console.log(res)
        console.log("\u7528\u6237\u7684code:" + res.code);
        wx.request({
          method: "POST",
          url: this.globalData.url + "user/login",
          data: {
            code: res.code
          },
          header: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          success: (res2) => {
            console.log("token : " + res2.data.data.token);
            this.globalData.token = res2.data.data.token;
            wx.setStorageSync("token", res2.data.data.token);
          }
        });
      }
    });
  },
  onShow: function() {
    console.log("App Show");
  },
  onHide: function() {
    console.log("App Hide");
  },
  globalData: {
    userInfo: null,
    url: "http://localhost:8080/",
    token: null,
    userBaseInfo:''
  }
})