<template>
	<view class="container">
		<view class="header">
			<view class="left">
				<view class="iconfont icon-jumao"></view>
			</view>
			<view class="middle">
				<view class="iconfont icon-31sousuo"></view>
				<input type="text" placeholder="查找游戏" :disabled="true" />
			</view>
			<view class="right">
				<view class="iconfont icon-lingsheng"></view>
			</view>
		</view>
		<view class="nav">
			<view class="left">
				<image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202305041438828.jpg"
					mode="aspectFill"></image>
			</view>
			<view class="right">
				<view class="hd">
					<image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202208191124156.png"
						mode="aspectFill"></image>
				</view>
				<div class="bd">
					<div class="bd_left">
						<image src="https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207142002825.jpg"
							mode="aspectFill"></image>
					</div>
					<div class="bd_right">
						<view class="iconfont icon-youjiantou"></view>
					</div>
				</div>
			</view>
		</view>
		<view class="scroll">
			<view class="content">
				<view class="left">
					<block v-for="(item, index) in list" :key="index">
						<view @click="toDetail(item)" v-if="index % 2 == 0" class="item">
							<image :src="item.image" mode="widthFix"></image>
							<view class="item_content">
								<view class="listTag">
									<view class="tag">
										<view class="iconfont icon-duigouxiao"></view>
										{{item.typeTagName}}
									</view>

								</view>
								<view class="title">{{ item.goodName }}</view>
								<view class="price">¥{{ item.goodPrice }}</view>
							</view>
						</view>
					</block>
				</view>
				<view class="right">
					<block v-for="(item, index) in list" :key="index">
						<view @click="toDetail(item)" v-if="index % 2 == 1">
							<image :src="item.image" mode="widthFix"></image>
							<view class="item_content">
								<view class="listTag">
									<view class="tag">
										<view class="iconfont icon-duigouxiao"></view>
										{{item.typeTagName}}
									</view>

								</view>
								<view class="title">{{ item.goodName }}</view>
								<view class="price">¥{{ item.goodPrice }}</view>
							</view>
						</view>
					</block>
				</view>
			</view>
			<!-- <template name="item">
				<view class="item">
					<view class="item-title-box">
						<view :url="url" class="item-title">{{ name }}</view>
					</view>
				</view>
			</template> -->
		</view>
		<tabbar :current="1"></tabbar>
	</view>
</template>

<script>
	import {
		reqAllProduct
	} from '@/api';
	export default {
		data() {
			return {
				list: [{
						image: 'https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202305041438828.jpg',
						title: '以闪亮之名，国服，开服号',
						price: 235
					},
					{
						image: 'https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207141144363.png',
						title: '以闪亮之名，国服，开服号',
						price: 235
					},
					{
						image: 'https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202305041438828.jpg',
						title: '以闪亮之名，国服，开服号',
						price: 235
					},
					{
						image: 'https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207141144363.png',
						title: '以闪亮之名，国服，开服号',
						price: 235
					},
					{
						image: 'https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202305041438828.jpg',
						title: '以闪亮之名，国服，开服号',
						price: 235
					},
					{
						image: 'https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207141144363.png',
						title: '以闪亮之名，国服，开服号',
						price: 235
					},
					{
						image: 'https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207141144363.png',
						title: '以闪亮之名，国服，开服号',
						price: 235
					}
				]
			};
		},
		methods: {
			toDetail(item) {
				uni.navigateTo({
					url: `/pages/detail/detail?goodId=${item.goodId}&ownUserId=${item.ownUserId}`
				});
			},
			async getList() {
				let res = await reqAllProduct();
				console.log(res);
				this.list = res;
			}
		},
		onShow() {
			this.getList();
		}
	};
</script>

<style lang="scss" scoped>
	.container {
		min-height: 100vh;
		background: #efefef;
		padding-bottom: 184rpx;

		.header {
			width: 100%;
			background: #808080;
			border-radius: 0rpx 0rpx 0rpx 0rpx;
			opacity: 1;
			padding-top: 88rpx;
			padding-bottom: 16rpx;
			display: flex;
			justify-content: space-around;
			align-items: center;

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
			max-width: 100%;
			height: 334rpx;
			background: #ffffff;
			border-radius: 24rpx 24rpx 24rpx 24rpx;
			margin: 32rpx 30rpx;
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
			padding: 0 30rpx 30rpx;
			box-sizing: border-box;

			.content {
				text-align: justify;
			}

			.item {
				background-color: #fff;
				margin: 10rpx 3%;
				margin-bottom: 20rpx;
				display: inline-block;
				width: 96%;
				border-radius: 20rpx;
				overflow: hidden;
				box-shadow: 0rpx 10rpx 30rpx rgba(0, 0, 0, 0.05);

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
					flex-flow: wrap;

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
						margin: 4rpx;

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

			.item {
				width: 334rpx;
				background: #ffffff;
				border-radius: 12rpx 12rpx 12rpx 12rpx;
				overflow: hidden;
			}
		}
	}
</style>