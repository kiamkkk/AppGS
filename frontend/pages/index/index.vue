<template>
	<view class="container">
		<view class="header">
			<view class="left"><view class="iconfont icon-jumao"></view></view>
			<view class="middle">
				<view class="iconfont icon-31sousuo"></view>
				<input type="text" placeholder="查找游戏" />
			</view>
			<view class="right"><view class="iconfont icon-lingsheng"></view></view>
		</view>
		<view class="nav">
			<view class="left"><image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202305041438828.jpg" mode="aspectFill"></image></view>
			<view class="right">
				<view class="hd"><image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202208191124156.png" mode="aspectFill"></image></view>
				<div class="bd">
					<div class="bd_left"><image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207142002825.jpg" mode="aspectFill"></image></div>
					<div class="bd_right"><view class="iconfont icon-youjiantou"></view></div>
				</div>
			</view>
		</view>
		<view class="scroll">
			<custom-waterfalls-flow :value="list" @wapperClick="toDetail" @imageClick="toDetail">
				<template v-slot:default="item">
					<view class="item" >
						<!-- <image :src="item.image" mode="widthFix"></image> -->
						<div class="item_content">
							<view class="listTag">
								<view class="tag">
									<view class="iconfont icon-duigouxiao"></view>
									找回包赔
								</view>
								<view class="tag">
									<view class="iconfont icon-duigouxiao"></view>
									官方自营
								</view>
								<view class="tag">
									<view class="iconfont icon-duigouxiao"></view>
									平台保证
								</view>
							</view>
							<view class="title">{{item.title}}</view>
							<view class="price">¥{{item.price}}</view>
						</div>
					</view>
				</template>
			</custom-waterfalls-flow>
		</view>
		<tabbar :current="1"></tabbar>
	</view>
</template>

<script>
export default {
	data() {
		return {
			list:[
				{
					image:"https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202305041438828.jpg",
					title:"以闪亮之名，国服，开服号",
					price:235
				},
				{
					image:"https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207141144363.png",
					title:"以闪亮之名，国服，开服号",
					price:235
				},
				{
					image:"https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202305041438828.jpg",
					title:"以闪亮之名，国服，开服号",
					price:235
				},
				{
					image:"https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207141144363.png",
					title:"以闪亮之名，国服，开服号",
					price:235
				},
				{
					image:"https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202305041438828.jpg",
					title:"以闪亮之名，国服，开服号",
					price:235
				},
				{
					image:"https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207141144363.png",
					title:"以闪亮之名，国服，开服号",
					price:235
				},
				{
					image:"https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207141144363.png",
					title:"以闪亮之名，国服，开服号",
					price:235
				}
			]
		};
	},
	methods: {
		toDetail(){
			console.log(423423);
			uni.navigateTo({
				url:"/pages/detail/detail"
			})
		}
	}
};
</script>

<style lang="scss" scoped>
.container {
	min-height: 100vh;
	background: #efefef;
	padding-bottom: 184rpx;
	.header {
		width: 750rpx;
		background: #808080;
		border-radius: 0rpx 0rpx 0rpx 0rpx;
		opacity: 1;
		padding-top: 88rpx;
		padding-bottom: 16rpx;
		display: flex;
		justify-content: space-around;
		.middle {
			width: 472rpx;
			height: 66rpx;
			background: #e6e6e6;
			border-radius: 122rpx 122rpx 122rpx 122rpx;
			opacity: 1;
			display: flex;
			align-items: center;
			padding: 0 20rpx;
			.iconfont {
				font-size: 48rpx;
				margin-right: 20rpx;
				color: #737373;
			}
			input {
				flex: 1;
				font-size: 32rpx;
				color: #737373;
			}
		}
		.left,
		.right {
			.iconfont {
				font-size: 50rpx;
				color: #fff;
			}
		}
	}
	.nav {
		width: 684rpx;
		height: 334rpx;
		background: #ffffff;
		border-radius: 24rpx 24rpx 24rpx 24rpx;
		margin: 32rpx auto;
		padding: 16rpx 20rpx;
		box-sizing: border-box;
		display: flex;
		justify-content: space-between;
		.left {
			image {
				width: 256rpx;
				height: 302rpx;
				border-radius: 10rpx 10rpx 10rpx 10rpx;
				opacity: 1;
			}
		}
		.right {
			display: flex;
			flex-flow: column;
			justify-content: space-between;
			.hd {
				image {
					width: 372rpx;
					height: 156rpx;
					border-radius: 10rpx 10rpx 10rpx 10rpx;
					opacity: 1;
				}
			}
			.bd {
				display: flex;
				justify-content: space-between;
				.bd_left {
					image {
						width: 262rpx;
						height: 128rpx;
						border-radius: 10rpx 10rpx 10rpx 10rpx;
						opacity: 1;
					}
				}
				.bd_right {
					width: 90rpx;
					height: 128rpx;
					background: #808080;
					border-radius: 10rpx 10rpx 10rpx 10rpx;
					opacity: 1;
					display: flex;
					justify-content: center;
					align-items: center;
					.iconfont {
						font-size: 52rpx;
						color: #fff;
					}
				}
			}
		}
	}
	.scroll {
		padding:0 30rpx 30rpx;
		box-sizing: border-box;
		.item {
			width: 334rpx;
			background: #ffffff;
			border-radius: 12rpx 12rpx 12rpx 12rpx;
			overflow: hidden;
			image {
				width: 100%;
			}
			.item_content {
				padding: 16rpx;
				box-sizing: border-box;
			}
			.listTag {
				display: flex;
				align-items: center;
				.tag {
					display: flex;
					background: #ffffff;
					align-items: center;
					border-radius: 122rpx 122rpx 122rpx 122rpx;
					padding: 5rpx;
					opacity: 1;
					border: 2rpx solid #fb9797;
					font-size: 12rpx;
					font-family: PingFang SC-Regular, PingFang SC;
					font-weight: 400;
					color: #af6262;
					margin-right: 8rpx;
					white-space: nowrap;
					.iconfont {
						font-size: 13rpx;
						color: #f32e2e;
						margin-right: 4rpx;
					}
				}
			}
			.title {
				font-size: 24rpx;
				font-family: PingFang SC-Regular, PingFang SC;
				font-weight: 400;
				color: #000000;
				margin: 10rpx 0;
			}
			.price {
				font-size: 24rpx;
				font-family: PingFang SC-Regular, PingFang SC;
				font-weight: 400;
				color: #c5434b;
			}
		}
	}
}
</style>
