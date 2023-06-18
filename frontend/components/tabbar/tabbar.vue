<template>
	<view class="tabbar">
		<view class="tabbar_region">
			<view class="item" @click="toUrl('/pages/index/index')">
				<image src="/static//home_select.png" mode="" v-if="current === 1"></image>
				<image src="/static/home.png" mode="" v-else></image>
			</view>
			<view class="item">
				<image src="/static/shop_select2.png" mode=""  v-if="current === 2"></image>
				<image src="/static/shop2.png" mode="" v-else></image>
			</view>
			<view class="item">
				<image src="/static/add.png" mode=""></image>
				</view>
			<view class="item">
				<image src="/static/shop_icon.png" mode="" v-if="current === 3"></image>
				<image src="/static/shop.png" mode="" v-else></image>
			</view>
			<view class="item" @click="toUrl('/pages/me/me')">
				<image src="/static/me_select.png" mode="" v-if="current === 4"></image>
				<image src="/static/me.png" mode="" v-else></image>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	name: 'tabbar',
	data() {
		return {};
	},
	props:['current'],
	methods:{
		toUrl(url){
			uni.navigateTo({
				url
			})
		}
	}
};
</script>

<style lang="scss">
.tabbar {
	display: flex;
	justify-content: center;
	position: fixed;
	bottom: 52rpx;
	width: 100%;
	left: 0;
	.tabbar_region {
		width: 720rpx;
		height: 132rpx;
		background: #808080;
		box-shadow: 0rpx 8rpx 12rpx 0rpx rgba(0,0,0,0.25);
		opacity: 1;
		display: flex;
		justify-content: space-around;
		align-items: center;
		border-radius: 100rpx;
		.item {
			image {
				width: 48rpx;
				height: 50rpx;
				opacity: 1;
			}
		}
	}
}
</style>
