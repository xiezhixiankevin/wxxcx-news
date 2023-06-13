"use strict";
var common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      gender: "",
      birthday: "",
      nickname: "",
      school: "",
      college: "",
      description: "",
      avatarUrl: "",
      avatarIsChanged: false
    };
  },
  methods: {
    onChooseAvatar(e) {
      console.log("onChooseAvatar");
      const { avatarUrl } = e.detail;
      this.avatarUrl = avatarUrl;
      this.avatarIsChanged = true;
    },
    editNickname() {
    },
    editGender() {
      console.log("[[editGender]]");
      let genderList = ["\u7537", "\u5973"];
      wx.showActionSheet({
        itemList: genderList,
        success: (res) => {
          this.gender = genderList[res.tapIndex];
        },
        fail: (res) => {
          console.log(res.errMsg);
        }
      });
    },
    editSchool() {
    },
    editCollege() {
    },
    editBirthday(e) {
      this.birthday = e.detail.value;
    },
    editDescription() {
    },
    saveUserInfo() {
      if (this.avatarIsChanged) {
        wx.uploadFile({
          url: getApp().globalData.url + "user/uploadAvatar",
          filePath: this.avatarUrl,
          name: "file",
          header: {
            "content-type": "multipart/form-data",
            "Authorization": wx.getStorageSync("token")
          },
          success: (res) => {
            if (res.statusCode === 200) {
              this.avatarUrl = JSON.parse(res.data).data;
            }
          },
          fail: (res) => {
            console.log(res.errMsg);
          }
        });
      }
      wx.request({
        url: getApp().globalData.url + "user/saveUserInfo",
        method: "POST",
        header: {
          "content-type": "application/json",
          "Authorization": wx.getStorageSync("token")
        },
        data: {
          gender: this.gender,
          birthday: this.birthday,
          nickname: this.nickname,
          school: this.school,
          college: this.college,
          description: this.description
        },
        success: (res) => {
          if (res.statusCode === 200) {
            wx.showToast({
              title: "\u4FDD\u5B58\u6210\u529F",
              icon: "success",
              duration: 2e3,
              complete: () => {
                wx.navigateBack({
                  delta: 1
                });
              }
            });
          }
        },
        fail: (res) => {
          console.log(res.errMsg);
        }
      });
    }
  },
  onLoad(options) {
    const userBaseInfo = getApp().globalData.userBaseInfo;
    console.log(userBaseInfo);
    this.nickname = userBaseInfo.nickname;
    this.gender = userBaseInfo.gender;
    this.birthday = userBaseInfo.birthday;
    this.school = userBaseInfo.school;
    this.college = userBaseInfo.college;
    this.description = userBaseInfo.description;
    this.avatarUrl = userBaseInfo.avatarUrl;
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.o((...args) => $options.onChooseAvatar && $options.onChooseAvatar(...args)),
    b: "url(" + $data.avatarUrl + ")",
    c: $data.nickname,
    d: common_vendor.o(($event) => $data.nickname = $event.detail.value),
    e: common_vendor.o((...args) => $options.editNickname && $options.editNickname(...args)),
    f: common_vendor.t($data.gender || "\u8BF7\u9009\u62E9"),
    g: common_vendor.o((...args) => $options.editGender && $options.editGender(...args)),
    h: $data.school,
    i: common_vendor.o(($event) => $data.school = $event.detail.value),
    j: common_vendor.o((...args) => $options.editSchool && $options.editSchool(...args)),
    k: $data.college,
    l: common_vendor.o(($event) => $data.college = $event.detail.value),
    m: common_vendor.o((...args) => $options.editCollege && $options.editCollege(...args)),
    n: common_vendor.t($data.birthday || "\u8BF7\u9009\u62E9"),
    o: common_vendor.o((...args) => $options.editBirthday && $options.editBirthday(...args)),
    p: $data.birthday,
    q: common_vendor.o((...args) => _ctx.showDatePicker && _ctx.showDatePicker(...args)),
    r: $data.description,
    s: common_vendor.o(($event) => $data.description = $event.detail.value),
    t: $data.description.length > 16
  }, $data.description.length > 16 ? {} : {}, {
    v: common_vendor.o((...args) => $options.editDescription && $options.editDescription(...args)),
    w: common_vendor.o((...args) => $options.saveUserInfo && $options.saveUserInfo(...args))
  });
}
var MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "D:/dongji/dongji_miniprogram/pages/userInfo/userInfo.vue"]]);
wx.createPage(MiniProgramPage);
