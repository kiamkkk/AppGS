import Request from "@/js_sdk/luch-request/luch-request/index.js";
import {
	requestUrl
} from "../config";
const http = new Request({
	//设置请求的base url
	baseURL: requestUrl,
	//超时时长5分钟
	timeout: 300000,
	header: {
		"Content-Type": "application/json",
	},
});
//请求拦截器
http.interceptors.request.use(
	(config) => {
		uni.showLoading({
			title: "处理中..."
		})
		console.log(config)
		const token = uni.getStorageSync("token");
		if (token) {
			// console.log("请求", token)
			// 拦截添加token
			config.header = {
				Authorization: token,
				// Authorization: "Bearer eyJhbGciOiJIUzM4NCJ9.eyJncmEiOiJVU0VSIiwic3ViIjoidXNlcjIiLCJleHAiOjE2ODcxMDQyNTF9.MbFqH1canp61G0zWpgY42W_H5hWoQRnKu5Q9hEmoxxSO7pmWzTzuJOCV-AYodGAO",
			};
		}
		return config;
	},
	(error) => {
		return Promise.resolve(error);
	}
);
// 响应拦截器
http.interceptors.response.use(
	async (response) => {
			uni.hideLoading()
			return response.data;
		},
		(error) => {
			uni.hideLoading();
			uni.setStorageSync("userInfo", {})
			uni.setStorageSync('token', "");
			uni.setStorageSync("username", "");
			uni.navigateTo({
				url: "/pages/login/login"
			})
			console.log("响应拦截器错误捕获", error);
			return Promise.resolve(error);
		}
);
// 封装get器请求
export const get = async (url, data = {}) => {
	let res = await http.request({
		url,
		method: "GET",
		params: {
			...data,
		},
	});
	return Promise.resolve(res);
};
// 封装post请求
export const post = async (url, data = {}, params = {}) => {
	let res = await http.request({
		url,
		method: "POST",
		data: {
			...data,
		},
		params,
	});
	return Promise.resolve(res);
};
// 封装put请求
export const put = async (url, data = {}, params = {}) => {
	let res = await http.request({
		url,
		method: "PUT",
		data: {
			...data,
		},
		params,
	});
	return Promise.resolve(res);
};
// 封装del请求
export const del = async (url, params = {}) => {
	let res = await http.request({
		url,
		method: "DELETE",
		params,
	});
	if (res?.code === 200) {
		return Promise.resolve(res);
	} else {
		return Promise.reject(res);
	}
};