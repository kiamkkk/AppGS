<template>
	<view class="detail">
		<view class="header">
			<view class="nav">
				<view class="left" @click="toBack"><view class="iconfont icon-zuojiantou"></view></view>
				<view class="middle">以闪亮之名，国服，开服号</view>
				<view class="right"><view class="iconfont icon-likefill"></view></view>
			</view>
			<swiper :indicator-dots="true" :autoplay="true" indicator-active-color="#000000" indicator-color="#fff" :interval="3000" :duration="1000">
				<swiper-item>
					<view class="swiper-item"><image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207141142698.jpg" mode="aspectFill"></image></view>
				</swiper-item>
				<swiper-item>
					<view class="swiper-item"><image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207141142698.jpg" mode="aspectFill"></image></view>
				</swiper-item>
			</swiper>
		</view>
		<view class="userInfo">
			<view class="left">
				<image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202209112023144.jpeg" mode=""></image>
				<view class="info">
					<view class="name">测试用户</view>
					<view class="rate">
						<view class="label">卖家口碑:</view>
						<uni-rate allow-half :value="4.6" :size="16" activeColor="#000000" />
						<view class="number">4.6</view>
					</view>
				</view>
			</view>
			<view class="right">
				<view class="right_text">卖家主页</view>
				<view class="iconfont icon-youjiantou1"></view>
			</view>
		</view>
		<view class="product_info">
			<view class="item">
				<text>商品编号:</text>
				Y2304170551
			</view>
			<view class="item">
				<text>游戏区服:</text>
				以闪亮之名国服糖花蜜语服务器
			</view>
			<view class="item">
				<text>衣服数量:</text>
				117
			</view>
			<view class="item">
				<text>妆容数量:</text>
				304
			</view>
			<view class="item">
				<text>账号等级:</text>
				34
			</view>
			<view class="item">
				<text>商品描述:</text>
				退游了不想玩了，可小刀，家居栗子还有5w，普通池子和限定池的衣服都差不多抽齐了，羁绊有5张SR，主线打到第十章左右
			</view>
		</view>
		<view class="detail">
			<view class="detail_title">商品截图</view>
			<image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202305041438828.jpg" mode="widthFix"></image>
			<image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202305041438828.jpg" mode="widthFix"></image>
			<image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202305041438828.jpg" mode="widthFix"></image>
			<image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202305041438828.jpg" mode="widthFix"></image>
			<image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202305041438828.jpg" mode="widthFix"></image>
		</view>
		<view class="arrow_region">
			<view class="item">
				<image src="/static/message_icon.png" mode=""></image>
				<view class="text">联系卖家</view>
			</view>
			<view class="item">
				<image src="/static/shop.png" mode=""></image>
				<view class="text">加入购物车</view>
			</view>
			<view class="btn">立即购买</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {};
	},
	methods: {
		toBack() {
			uni.navigateBack();
		}
	}
};
</script>

<style lang="scss">
.detail {
	.header {
		width: 750rpx;
		height: 646rpx;
		position: relative;
		.nav {
			position: absolute;
			top: 102rpx;
			left: 0;
			width: 100%;
			display: flex;
			justify-content: space-between;
			padding: 0 48rpx;
			box-sizing: border-box;
			align-items: center;
			z-index: 99;
			.left {
				.iconfont {
					font-size: 52rpx;
					color: #000;
				}
			}
			.right {
				.iconfont {
					font-size: 52rpx;
					color: #000;
				}
			}
			.middle {
				font-size: 40rpx;
				font-family: PingFang SC-Regular, PingFang SC;
				font-weight: 400;
				color: #000000;
			}
		}
		swiper {
			width: 750rpx;
			height: 646rpx;
			image {
				width: 750rpx;
				height: 646rpx;
			}
		}
	}
	.userInfo {
		padding: 32rpx;
		box-sizing: border-box;
		display: flex;
		justify-content: space-between;
		align-items: center;
		.left {
			display: flex;
			align-items: center;
			image {
				width: 104rpx;
				height: 104rpx;
				border-radius: 50%;
				margin-right: 16rpx;
			}
			.info {
				.name {
					font-size: 40rpx;
					font-family: PingFang SC-Semibold, PingFang SC;
					font-weight: 600;
					color: #404040;
				}
				.rate {
					display: flex;
					align-items: center;
					.label {
						font-size: 24rpx;
						font-family: PingFang SC-Semibold, PingFang SC;
						font-weight: 600;
						color: #737373;
					}
					.number {
						font-size: 24rpx;
						font-family: PingFang SC-Semibold, PingFang SC;
						font-weight: 600;
						color: #737373;
					}
				}
			}
		}
		.right {
			display: flex;
			align-items: center;
			.right_text {
				font-size: 24rpx;
				font-family: PingFang SC-Semibold, PingFang SC;
				font-weight: 600;
				color: #737373;
			}
			.iconfot {
				font-size: 32rpx;
				margin-left: 10rpx;
				color: #737373;
			}
		}
	}
	.product_info {
		width: 686rpx;
		background: #fafafa;
		border-radius: 20rpx 20rpx 20rpx 20rpx;
		opacity: 1;
		margin: auto;
		padding: 30rpx 32rpx;
		box-sizing: border-box;
		.item {
			font-size: 24rpx;
			font-family: PingFang SC-Semibold, PingFang SC;
			font-weight: 600;
			color: #262626;
			margin-bottom: 16rpx;
			text {
				color: #737373;
			}
		}
		.item:last-child {
			margin-bottom: 0;
		}
	}
	.detail {
		width: 686rpx;
		background: #fafafa;
		border-radius: 20rpx 20rpx 20rpx 20rpx;
		opacity: 1;
		margin: 16rpx auto;
		padding: 16rpx;
		box-sizing: border-box;
		.detail_title {
			font-size: 24rpx;
			font-family: PingFang SC-Semibold, PingFang SC;
			font-weight: 600;
			color: #737373;
		}
		image {
			width: 100%;
			margin-top: 8rpx;
		}
	}
	.arrow_region {
		width: 720rpx;
		height: 132rpx;
		background: #808080;
		box-shadow: 0rpx 8rpx 12rpx 0rpx rgba(0, 0, 0, 0.25);
		opacity: 1;
		position: fixed;
		bottom: 52rpx;
		left: 16rpx;
		display: flex;
		justify-content: space-around;
		align-items: center;
		border-radius: 134rpx;
		.item {
			display: flex;
			flex-flow: column;
			align-items: center;
			image {
				width: 52rpx;
				height: 52rpx;
			}
			.text {
				font-size: 28rpx;
				font-family: PingFang SC-Regular, PingFang SC;
				font-weight: 400;
				color: #fafafa;
			}
		}
		.btn {
			width: 288rpx;
			height: 90rpx;
			background: #ffa5a5;
			border-radius: 134rpx 134rpx 134rpx 134rpx;
			opacity: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			color: #fff;
		}
	}
}
</style>
