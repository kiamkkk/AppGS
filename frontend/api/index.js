import {
	get,
	post,
	put,
	del
} from "./request.js";

// 用户登录
export const login = (data) => post("/users", data)
// 管理员登录
export const adminLogin = (data) => post("/admin", data)
// 获取用户信息
export const reqUserInfo = (data) => get(`/users/${data.username}`)
// 获取所有商品
export const reqAllProduct = () => get("/goods")

// 获取某个商品的对话的全部聊天记录
export const reqChatRecords	 = ({good_id, user_id}) => get(`/chats/records/${good_id}/${user_id}`)
