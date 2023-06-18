<template>
	<view class="view-body" :style="{'--statusBar': `${statusBar}px`,'--keyboardHeight': `${keyboardHeight}px`}">
		<view class="header">
			<view class="left">
				<span @click="toBack" class="iconfont icon-youjiantou2"></span>
				<view class="userInfo">
					<h4>uidhhdn</h4>
					<p>用户ID: s****5</p>
				</view>
			</view>
			<view class="right">
				<span class="set"></span>
			</view>
		</view>

		<view class="shopCard" @click="closeFuns">
			<view class="left"
				style="background-image: url(https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207141142698.jpg);">
			</view>
			<view class="right">
				<view class="top">
					<span>￥199</span>
					<p>以闪亮之名，国服，开服号</p>
				</view>
				<view class="bottom">
					<span>商品编号：{{goodId}}</span>
					<!-- 					<span>商品编号：Y2304170551</span> -->
					<button>立即购买</button>
				</view>
			</view>
		</view>
		<view class="scroll-chat" @touchstart="closeFuns" @click="closeFuns">
			<scroll-view :scroll-with-animation="true" :bounces="false" scroll-y="true" :scroll-top="screenTop"
				class="scroll-view">
				<template v-for="item, index in chatData">
					<view
						v-if="!chatData[index-1] || (chatData[index-1] && item.time - chatData[index-1].time  > 1000*60)"
						class="time">
						{{timeFormate(item.time)}}
					</view>
					<view :class="item.fromUserName != 'mine' ? 'other':'mine'">
						<view class="head-img" :style="`background-image: url(${baseUrl+userInfo.headSculpture})`">
							<!-- <view class="head-img" :style="`background-image: url(${item.picture})`"> -->
						</view>
						<view v-if="item.text" class="text">
							{{ item.text }}
						</view>
						<view v-else class="img">
							<image @click="showImg(item.picture)" mode="widthFix" :src="item.picture" alt=""
								srcset="" />
						</view>
					</view>
				</template>
				<view style="height: 50rpx;">
				</view>
			</scroll-view>
		</view>
		<view class="chatboxes">
			<input :focus="sendInputFocus" :confirm-hold="true" ref="sendInput" type="text" confirm-type="send"
				v-model="text" :adjust-position="false" placeholder="输入你想对TA说的话吧..." @confirm="sendText"
				@keyboardheightchange="KeyboardEvent" @focus="isShowFuns = false" />
			<view class="send" @touchend.prevent="sendText" @click="sendText" v-if="text.trim()">
				<span>发送</span>
			</view>
			<button :class="{close: isShowFuns}" class="funs" v-else @click="toggleFuns">+</button>
		</view>
		<view class="functions" :class="{animation: isAnimation}" :style="{height: isShowFuns? '240rpx':'0rpx'}">
			<view class="select-img" @click="selectImg">
				<view class="icon">
					<img src="static/capture.png" alt="" srcset="" />
				</view>
				<span>发送图片</span>
			</view>
		</view>

		<view class="scaleImg" v-show="isShowImgBig" @click="hiddenImg">
			<movable-area>
				<movable-view :scale-value="scale" direction="all" :animation="false" scale-min="1" scale="true"
					scale-max="4">
					<image :src="showImgUrl" mode="widthFix"></image>
				</movable-view>
			</movable-area>
		</view>
	</view>
</template>

<script>
	import {
		reqChatRecords,
		reqUserInfo
	} from '@/api'
	import {
		requestUrl
	} from "@/config/index.js"
	export default {
		data() {
			return {
				goodId: 4,
				text: '',
				statusBar: 0,
				keyboardHeight: 0,
				isShowFuns: false,
				Img: '',
				isShowImgBig: false,
				sendInputFocus: false,
				isAnimation: true,
				showImgUrl: '',
				scale: 2,
				screenTop: 9999,
				chatData: [],
				userInfo: uni.getStorageSync("userInfo") || {},
				baseUrl: requestUrl
			};
		},
		async onLoad(query) {
			this.goodId = query.goodId
			this.ownUserId = query.ownUserId
			const data = await reqChatRecords({
				good_id: this.goodId,
				user_id: 25
			})

			if (this.userInfo.headSculpture == undefined) {
				const userInfoRes = await reqUserInfo({})
				this.userInfo = userInfoRes
			}
			
			const token = uni.getStorageSync("token")
			uni.connectSocket({
				url: "ws://118.178.243.30:8080/websocket",
				header: {
					'content-type': 'application/json',
					// "Authorization": "Bearer eyJhbGciOiJIUzM4NCJ9.eyJncmEiOiJVU0VSIiwic3ViIjoidXNlcjY2IiwiZXhwIjoxNjg3MDk1NTM4fQ.1kA5dVkytmDDDAQqdSTlQ5Vq2TXJ3Y8omVbaCM38Wu3YgDwU07EPdUYWAGEGk7Iy", // user66
					"Authorization": token, // user66
				},
			})
			const that = this
			uni.onSocketOpen(function(res) {
				that.socketOpen = true
				console.log('WebSocket连接已打开！');
			});

			const {
				goodId
			} = this

			uni.getSystemInfo({
				success: (e) => {
					console.log("????")
					that.statusBar = e.statusBarHeight;
				}
			})

			this.chatData.sort((a, b) => a.time - b.time)
		},
		onHide() {
			const {
				goodId,
				chatData
			} = this
			uni.setStorageSync(`chat_${goodId}`, JSON.stringify(chatData))
		},
		methods: {
			toBack() {
				uni.navigateBack();
			},
			toggleFuns() {
				this.isShowFuns = !this.isShowFuns
			},
			closeFuns() {
				this.isShowFuns = false
			},
			KeyboardEvent(e) {
				this.isAnimation = e.target.height == 0
				this.keyboardHeight = e.target.height
				this.screenTop = 999
				setTimeout(() => {
					this.$nextTick(() => {
						this.screenTop = 9999
					})
				}, 10)
			},
			sendText() {
				if (this.text.trim() == '')
					return uni.showToast({
						title: '发送内容不可为空',
						duration: 2000,
						icon: 'none'
					});
				this.screenTop = 999
				setTimeout(() => {
					this.$nextTick(() => {
						this.sendInputFocus = true
						this.screenTop = 9999
					})
				}, 10)
				const text = this.text

				if (this.socketOpen) {
					const data = {
						fromUserId: "25",
						toUserId: String(this.ownUserId),
						goodId: String(this.goodId),
						fromUserName: "user66",
						type: "CHAT_TEXT",
						message: String(text),
						time: String(Date.now())
					}
					uni.sendSocketMessage({
						data: JSON.stringify(data),
						fail: err => {
							console.log(err, "err")
						},
						success: (res) => {
							console.log(res, "res")
						}
					})
				}

				this.chatData.push({
					time: new Date().getTime(),
					fromUserName: "mine",
					text,
					picture: "https://raw.sevencdn.com/weiyongling/xiaoweiPictures/master/202207141142698.jpg"
				})
				this.text = ''
			},
			selectImg() {
				const that = this
				uni.chooseImage({
					count: 1,
					success: (e) => {
						that.chatData.push({
							time: new Date().getTime(),
							fromUserName: "mine",
							picture: e.tempFilePaths[0]
						})
						setTimeout(() => {
							that.screenTop = 999
							that.$nextTick(() => {
								that.screenTop = 9999
							})
						}, 100)
					}
				})
			},
			showImg(url) {
				this.isShowImgBig = true
				this.showImgUrl = url
				this.scale = 0
				this.$nextTick(function() {
					this.scale = 1
					this.isShowFuns = false
				})
			},
			hiddenImg() {
				this.isShowImgBig = false
				this.showImgUrl = ''
			},
			addZeroes(num) {
				num = String(num); // 转换为字符串  
				return num.length < 2 ? '0' + num : num; // 如果长度小于2，则在前面添加0，否则返回原字符串  
			},
			isThisYear(time, now = new Date()) {
				const startOfYear = new Date(now.getFullYear(), 0, 1).getTime();
				const endOfYear = new Date(now.getFullYear() + 1, 0, 1).getTime() - 1;
				return (time >= startOfYear && time <= endOfYear)
			},
			isToday(time, now = new Date()) {
				var targetDate = new Date(time);
				const year = targetDate.getFullYear();
				const month = targetDate.getMonth() + 1; // 月份从0开始，需要加1  
				const day = targetDate.getDate();

				return (year == now.getFullYear() && month == now.getMonth() + 1 && day == now.getDate())
			},
			timeFormate(time) {
				const date = new Date(time)
				const now = new Date()
				const year = date.getFullYear()
				const month = this.addZeroes(date.getMonth() + 1)
				const date2 = this.addZeroes(date.getDate())
				const hours = this.addZeroes(date.getHours())
				const minutes = this.addZeroes(date.getMinutes())

				if (this.isToday(time, now)) {
					return `${hours}:${minutes}`
				} else if (!this.isThisYear(time, now)) {
					return `${year}-${month}-${date2} ${hours}:${minutes}`
				}
				return `${month}-${date2} ${hours}:${minutes}`
			}
		},
		onBackPress() {
			if (this.isShowImgBig == true)
				return this.isShowImgBig = false, true
			if (this.isShowFuns == true)
				return this.isShowFuns = false, true
		},
	}
</script>

<style lang="scss" scoped>
	.view-body {
		display: flex;
		flex-direction: column;
		height: calc(100vh - var(--keyboardHeight));
	}

	.header {
		height: 110rpx;
		min-height: 110rpx;
		display: flex;
		justify-content: space-between;
		box-sizing: border-box;
		padding: 0 32rpx;
		align-items: center;
		margin-top: var(--statusBar);

		.left {
			display: flex;
			align-items: center;

			.iconfont {
				width: 50rpx;
				text-align: center;
				transform: rotate(180deg);
				font-size: 32rpx;
				font-weight: bolder;
			}

			.userInfo {
				margin-left: 24rpx;

				h4 {
					width: 254rpx;
					font-size: 38rpx;
					font-weight: bolder;
					color: #000000;
					margin: 0;
					padding: 0;
				}

				p {
					width: 508rpx;
					font-size: 26rpx;
					font-weight: 400;
					color: #595959;
					margin: 0;
					padding: 0;
				}
			}
		}

		.right .set {
			width: 14rpx;
			height: 14rpx;
			background: black;
			display: inline-block;
			border-radius: 14rpx;
			position: relative;
			margin: 0 20px;

			&::before {
				content: "";
				width: 14rpx;
				height: 14rpx;
				background: black;
				display: inline-block;
				border-radius: 14rpx;
				position: absolute;
				top: 0;
				left: -20rpx;
			}

			&::after {
				content: "";
				width: 14rpx;
				height: 14rpx;
				background: black;
				display: inline-block;
				border-radius: 14rpx;
				position: absolute;
				right: -20rpx;
			}
		}
	}

	.shopCard {
		height: 180rpx;
		min-height: 180rpx;
		margin-top: 8rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		box-sizing: border-box;
		padding: 0 24rpx;

		.left {
			width: 140rpx;
			height: 140rpx;
			background-repeat: no-repeat;
			background-size: cover;
			background-position: center;
			border-radius: 16rpx 16rpx 16rpx 16rpx;
		}

		.right {
			width: 140rpx;
			height: 140rpx;
			display: flex;
			flex-direction: column;
			flex: 1;
			margin-left: 32rpx;

			.top {
				span {
					display: block;
					width: 94rpx;
					height: 40rpx;
					font-size: 32rpx;
					font-weight: 400;
					color: #c5434B;
				}

				p {
					height: 40rpx;
					font-size: 24rpx;
					font-family: PingFang SC-Regular, PingFang SC;
					font-weight: 400;
					color: #262626;
					line-height: 40rpx;
					white-space: nowrap;
					overflow: hidden;
					text-overflow: ellipsis;
				}
			}

			.bottom {
				display: flex;
				align-items: center;
				justify-content: space-between;

				span {
					width: 306rpx;
					height: 34rpx;
					font-size: 24rpx;
					font-family: PingFang SC-Semibold, PingFang SC;
					font-weight: 600;
					color: #737373;
					line-height: 28rpx;
					white-space: nowrap;
					overflow: hidden;
					text-overflow: ellipsis;
				}

				button {
					width: 176rpx;
					height: 60rpx;
					background-color: #FF8787;
					border-radius: 134rpx 134rpx 134rpx 134rpx;
					font-size: 28rpx;
					font-weight: 600;
					color: #FAFAFA;
					line-height: 60rpx;
					margin: 0;
				}
			}
		}
	}


	.scroll-chat {
		// height: calc(100vh - 110rpx - 120rpx - 188rpx - var(--statusBar) - var(--keyboardHeight));
		flex: 1;
		position: relative;

		.scroll-view {
			position: absolute;
			height: 100%;
		}

		.time {
			padding-top: 48rpx;
			text-align: center;
			height: 34rpx;
			font-size: 24rpx;
			font-family: PingFang SC-Semibold, PingFang SC;
			font-weight: 600;
			color: #737373;
			line-height: 28rpx;
		}

		.mine,
		.other {
			display: flex;
			box-sizing: border-box;
			padding: 0 40rpx;
			padding-top: 48rpx;

			.head-img {
				width: 88rpx;
				min-width: 88rpx;
				height: 88rpx;
				border-radius: 88rpx;
				background-repeat: no-repeat;
				background-size: cover;
				background-position: center;
			}

			.text {
				box-sizing: border-box;
				padding: 20rpx 38rpx;
				background: #FFFFFF;
				border-radius: 24rpx 24rpx 24rpx 24rpx;
				margin: 0 20rpx;
			}

			.img {
				box-sizing: border-box;
				margin: 0 20rpx;
				overflow: hidden;

				image {
					border-radius: 24rpx 24rpx 24rpx 24rpx;
					max-width: 40%;
				}
			}
		}

		.mine {
			.text {
				color: white;
				background-color: #808080;
			}

			flex-direction: row-reverse;

			image {
				float: right;
			}
		}
	}

	.chatboxes {
		height: 120rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		box-sizing: border-box;
		padding: 0 32rpx;

		input {
			flex: 1;
			height: 76rpx;
			background: #808080;
			border-radius: 120rpx 120rpx 120rpx 120rpx;
			opacity: 1;
			color: white;
			font-size: 30rpx;
			box-sizing: border-box;
			padding: 0 30rpx;
			margin-right: 30rpx;
			transition: all 0.2s;
		}

		.send {
			cursor: pointer;
			height: 100%;
			display: flex;
			align-items: center;
			justify-content: center;

			span {
				font-size: 24rpx;
				padding: 0 30rpx;
				line-height: 64rpx;
				height: 64rpx;
				border-radius: 76rpx;
				display: flex;
				background-color: black;
				color: white;

			}
		}

		button.funs {
			width: 64rpx;
			height: 64rpx;
			border-radius: 64rpx;
			background: #000000;
			color: white;
			line-height: 60rpx;
			margin: 0;
			text-align: center;
			padding: 0;
			font-size: 40rpx;
			font-weight: bolder;
			transition: transform 0.2s;

			&.close {
				transform: rotate(45deg);
			}
		}

		.uni-input-placeholder {
			color: white;
		}
	}

	.functions {
		display: flex;
		box-sizing: border-box;
		padding: 0 32rpx;
		overflow: hidden;

		&.animation {
			transition: height 0.4s;
		}

		.select-img {
			margin-top: 30rpx;
			display: flex;
			flex-direction: column;

			.icon {
				width: 80rpx;
				height: 80rpx;
				padding: 20rpx;
				border-radius: 30rpx;
				background-color: white;

				img {
					width: 80rpx;
				}
			}

			span {
				display: block;
				white-space: nowrap;
				font-size: 24rpx;
				line-height: 24rpx;
				margin-top: 10rpx;
				text-align: center;
			}
		}
	}



	.scaleImg {
		position: fixed;
		background-color: #000000db;
		height: 100vh;
		width: 750rpx;

		&>movable-area {
			height: 100vh;
			width: 100%;
			overflow: hidden;
			left: 0;
			z-index: 1000;

			movable-view {
				display: flex;
				justify-content: center;
				align-items: center;
				width: 100%;
				height: 100%;

				image {
					width: 750rpx;
				}
			}

		}
	}
</style>