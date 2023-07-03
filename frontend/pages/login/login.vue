<template>
	<view class="login">
		<view class="logo"><view class="iconfont icon-jumao"></view></view>
		<view class="title">登录/注册</view>
		<view class="form">
			<view class="item"><input type="text" placeholder="请输入账号" v-model="userName" /></view>
			<view class="item"><input type="password" placeholder="请输入密码" v-model="password" /></view>
		</view>
		<view class="arrow_region">
			<view class="btn" @click="submit">用户登录</view>
			<view class="btn" @click="submit2">管理员登录</view>
		</view>
	</view>
</template>

<script>
import { login, adminLogin } from '@/api';
export default {
	data() {
		return {
			userName: '',
			password: ''
		};
	},
	methods: {
		async submit() {
			const { userName, password } = this;
			if (!userName) return uni.showMsg('请输入账号');
			if (!password) return uni.showMsg('请输入密码');
			let res = await login({ userName, password });
			if(res.token){
				uni.setStorageSync('token',res.token);
				uni.setStorageSync('username',userName);
				console.log(res.token)
				uni.showMsg("登录成功",'success');
				setTimeout(()=>{
					uni.navigateBack()
				},1500)
			}else{
				uni.showMsg("账号或密码错误")
			}
			console.log(res);
		},
		async submit2() {
			const { userName, password } = this;
			if (!userName) return uni.showMsg('请输入账号');
			if (!password) return uni.showMsg('请输入密码');
			let res = await adminLogin({ adminName: userName, adminPassword: password });
		}
	}
};
</script>

<style lang="scss" scoped>
.login {
	background-color: #262626;
	height: 100vh;
	overflow: hidden;
	.logo {
		text-align: center;
		// width: 200rpx;
		// height: 100rpx;
		margin: 270rpx auto 0;
		.iconfont {
			display: block;
			font-size: 162rpx;
			color: #fff;
		}
	}
	.title {
		font-size: 52rpx;
		font-family: PingFang SC-Semibold, PingFang SC;
		font-weight: 600;
		color: #ffffff;
		padding-top: 42rpx;
		text-align: center;
		display: block;
	}
	.form {
		display: flex;
		flex-flow: column;
		align-items: center;
		padding: 0 84rpx;
		box-sizing: border-box;

		.item {
			margin-top: 32rpx;
			input {
				width: 100%;
				background: #ffffff;
				border-radius: 120rpx 120rpx 120rpx 120rpx;
				opacity: 1;
				padding: 26rpx 0;
				font-size: 44rpx;
				font-family: PingFang SC-Semibold, PingFang SC;
				font-weight: 600;
				color: #666666;
				text-align: center;
			}
		}
	}
	.arrow_region {
		display: flex;
		align-items: center;
		padding: 0 84rpx;
		box-sizing: border-box;
		margin-top: 58rpx;
		.btn {
			background: #5a5a5a;
			border-radius: 120rpx 120rpx 120rpx 120rpx;
			opacity: 1;
			flex: 1;
			font-size: 44rpx;
			font-family: PingFang SC-Semibold, PingFang SC;
			font-weight: 600;
			color: #ffffff;
			padding: 28rpx 0;
			text-align: center;
		}
		.btn:first-child {
			margin-right: 20rpx;
		}
	}
}
</style>
