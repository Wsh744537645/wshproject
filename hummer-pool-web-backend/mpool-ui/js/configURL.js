/*url 公共前缀 configURL.js 文件*/
var mpoolUI_url = '/apis';

//密码
var pwdValidate = {
	//test:/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$/,
	test: {
		test: function (str) {
			if (str.length <= 16) {
				return /^.*(?=.{6,})(?=.*[a-zA-Z]).*$/.test(str)
			}
			return false
		}
	},
	tips: '密码至少包含一个字母、长度为6至16位',
	cannot: '密码不能为空'
}

//用户名
var userNameValidate = {
	//test:/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$/,
	test: /^[a-zA-Z0-9]{6,16}$/,
	tips: '用户名由大小写字母或数字组成，且长度至少为 6至16位',
	usable: '用户名可用',
	cannot: '用户名不能为空'
}

//邮箱
var emailValidate = {
	test: /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,64})$/,
	tips: '请输入正确的邮箱！',
	usable: '邮箱可用',
	cannot: '邮箱不能为空'
}

//手机号码
var phoneValidate = {
	test: /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(16[5])|(17[0|3|7|8])|(18[0,5-9])|(19[6|8|9]))\d{8}$/,
	tips: '请输入正确的手机号码！',
	usable: '手机号码可用',
	cannot: '手机号码不能为空'
}