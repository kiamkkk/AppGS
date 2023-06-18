<template>
	<view class="me">
		<view class="header"></view>
		<view class="noLogin" @click="toLogin" v-if="!userInfo.userId">
			<view class="logo">登录</view>
			<view class="right">
				<view class="text">登录享受全部功能</view>
				<view class="iconfont icon-youjiantou1"></view>
			</view>
		</view>
		<view class="login" v-else>
			<view class="userInfo">
				<view class="avatar"><image :src="baseUrl+userInfo.headSculpture" mode=""></image></view>
				<view class="info">
					<view class="name">{{username}}</view>
					<view class="id">用户ID：{{userInfo.userId}}</view>
				</view>
			</view>
			<view class="right"><view class="iconfont icon-youjiantou1"></view></view>
		</view>
		<view class="body">
			<view class="nav">
				<view class="nav_item">
					<view class="iconfont icon-likefill"></view>
					<view class="text">我的收藏</view>
				</view>
				<view class="nav_item">
					<view class="iconfont icon-shizhongfill"></view>
					<view class="text">浏览历史</view>
				</view>
				<view class="nav_item">
					<view class="iconfont icon-iconfontyouxihudong"></view>
					<view class="text">我的游戏</view>
				</view>
				<view class="nav_item">
					<view class="iconfont icon-licai"></view>
					<view class="text">余额￥0.00</view>
				</view>
			</view>
			<view class="my card">
				<view class="title">
					<view class="title_text">我的交易</view>
					<view class="title_right"><view class="iconfont icon-youjiantou1"></view></view>
				</view>
				<view class="list">
					<view class="li">
						<view class="iconfont icon-shangpin"></view>
						<view class="text">我发布的</view>
					</view>
					<view class="li">
						<view class="iconfont icon-maichu"></view>
						<view class="text">我卖出的</view>
					</view>
					<view class="li">
						<view class="iconfont icon-womaidaode"></view>
						<view class="text">我买到的</view>
					</view>
					<view class="li">
						<view class="iconfont icon-tuikuan"></view>
						<view class="text">退款/售后</view>
					</view>
				</view>
			</view>
			<view class="operation card">
				<view class="title"><view class="title_text">常用功能</view></view>
				<view class="list">
					<view class="li">
						<view class="iconfont icon-kefu"></view>
						<view class="text">帮助与客服</view>
					</view>
					<view class="li">
						<view class="iconfont icon-icon_commone_shimingrenzhengmian"></view>
						<view class="text">实名认证</view>
					</view>
					<view class="li">
						<view class="iconfont icon-biaoqingbao-tianchong1"></view>
						<view class="text">表情包</view>
					</view>
					<view class="li">
						<view class="iconfont icon-xingqiudiqiu"></view>
						<view class="text">广场</view>
					</view>
					<view class="li">
						<view class="iconfont icon-shezhi"></view>
						<view class="text">设置</view>
					</view>
				</view>
			</view>
		</view>
		<tabbar :current="4"></tabbar>
	</view>
</template>

<script>
	import {reqUserInfo} from "@/api"
	import {requestUrl} from "@/config/index.js"
export default {
	data() {
		return {
			userInfo:uni.getStorageSync("userInfo") ||  {},
			username:uni.getStorageSync("username"),
			baseUrl:requestUrl
		};
	},
	methods:{
		toLogin(){
			uni.navigateTo({
				url:"/pages/login/login"
			})
		},
		async getUserInfo(){
			let username = uni.getStorageSync("username")
			let res = await reqUserInfo({username});
			if(res.userId){
				this.userInfo = res;
				uni.setStorageSync('userInfo',res)
			}else{
				uni.showMsg("获取用户信息失败")
			}
			console.log(res);
		}
	},
	onShow() {
		if(JSON.stringify(this.userInfo) === '{}' && uni.getStorageSync('token')){
			this.getUserInfo();
		}
	}
	
};
</script>

<style lang="scss">
.me {
	min-height: 100vh;
	background-color: #808080;
	.header {
		height: 88rpx;
		width: 100%;
	}
	.noLogin {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 16rpx 32rpx;
		box-sizing: border-box;
		.logo {
			width: 176rpx;
			height: 176rpx;
			background: #c4c4c4;
			opacity: 1;
			display: flex;
			align-items: center;
			justify-content: center;
			font-weight: 600;
			color: #ffffff;
			font-family: PingFang SC-Semibold, PingFang SC;
			border-radius: 50%;
		}
		.right {
			display: flex;
			align-items: center;
			.text {
				font-family: PingFang SC-Semibold, PingFang SC;
				font-weight: 600;
				color: #000000;
				font-size: 48rpx;
			}
			.iconfont {
				font-size: 64rpx;
			}
		}
	}
	.login {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 16rpx 32rpx;
		box-sizing: border-box;
		.userInfo {
			flex: 1;
			display: flex;
			align-items: center;
		}
		.avatar {
			margin-right: 32rpx;
			image {
				width: 176rpx;
				height: 176rpx;
				border-radius: 50%;
			}
		}
		.info {
			.name {
				font-size: 56rpx;
				font-family: PingFang SC-Semibold, PingFang SC;
				font-weight: 600;
				color: #000000;
			}
			.id {
				font-size: 32rpx;
				font-family: PingFang SC-Regular, PingFang SC;
				font-weight: 400;
				color: #595959;
				margin-top: 20rpx;
			}
		}
		.right {
			font-size: 48rpx;
		}
	}
	.body {
		background-color: #999999;
		border-radius: 50rpx 50rpx 0 0;
		padding: 32rpx 24rpx;
		box-sizing: border-box;
		min-height: 1500rpx;
		margin-top: 8rpx;
		.nav {
			display: flex;
			justify-content: space-around;
			.nav_item {
				display: flex;
				flex-flow: column;
				align-items: center;
				color: #fff;
				.iconfont {
					font-size: 56rpx;
				}
				.text {
					font-size: 28rpx;
					font-family: PingFang SC-Regular, PingFang SC;
					font-weight: 400;
					color: #fafafa;
					margin-top: 10rpx;
				}
			}
		}
		.card{
			padding: 16rpx;
			box-sizing: border-box;
			max-width: 100%;
			background: #FFFFFF;
			opacity: 0.75;
			margin: 24rpx 0;
			border-radius: 20rpx;
			.title{
				display: flex;
				justify-content: space-between;
				.title_text{
					font-size: 36rpx;
					font-family: PingFang SC-Regular, PingFang SC;
					font-weight: 400;
					color: #000000;
				}
				.title_right{
					.iconfont{
						font-size: 64rpx;
					}
				}
			}
			.list{
				display: flex;
				flex-flow: wrap;
				
				.li{
					width: 25%;
					display: flex;
					flex-flow: column;
					align-items: center;
					margin-top: 44rpx;
					.iconfont{
						font-size: 52rpx;
					}
					.text{
						font-size: 28rpx;
						font-family: PingFang SC-Regular, PingFang SC;
						font-weight: 400;
						color: #595959;
						margin-top: 10rpx;
					}
				}
			}
		}
	}
}
</style>
