package com.zsm.foxconn.mypaperless.help;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 轮播图片基础
 */
public class ViewPagerBarnner extends RelativeLayout implements
		OnPageChangeListener {

	private ViewPager viewPager;

	private LinearLayout indicatorView;
	private Context context;

	private int pageCount;

	private int numberPages;

	private LinkedList<Bitmap> imageUrls = new LinkedList<Bitmap>();

	private ViewPagerClick viewPagerClick;

	private float indicatorSize;

	private int indicatorDrawable;

	private float indicatorInterval;

	private float containerHeight;

	private float indicatorMarginBottom;

	private int indicatorBackgroud;

	private float indicatorBigSize;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			int currentPosition = viewPager.getCurrentItem();
			currentPosition = (currentPosition + 1) % pageCount;
			if (currentPosition == pageCount - 1) {
				viewPager.setCurrentItem(numberPages - 1, false);
			} else {
				viewPager.setCurrentItem(currentPosition);
			}
			mHandler.sendEmptyMessageDelayed(0, 3000);
		}
	};

	public ViewPagerBarnner(Context context) {
		this(context, null);
	}

	public ViewPagerBarnner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
//		TypedArray typedArray = context.obtainStyledAttributes(attrs,
//				R.styleable.ViewPager);
//		indicatorSize = typedArray.getDimension(
//				R.styleable.ViewPager_indicatorSize, 10);
//		indicatorBigSize = typedArray.getDimension(
//				R.styleable.ViewPager_indicatorBigSize, 0);
//		indicatorInterval = typedArray.getDimension(
//				R.styleable.ViewPager_indicatorInterval, 15);
//		indicatorDrawable = typedArray.getResourceId(
//				R.styleable.ViewPager_indicatorDrawable, 0);
//		indicatorBackgroud = typedArray.getResourceId(
//				R.styleable.ViewPager_indicatorBackgroud, 0);
//		containerHeight = typedArray.getDimension(
//				R.styleable.ViewPager_containerHeight, 20);
//		indicatorMarginBottom = typedArray.getDimension(
//				R.styleable.ViewPager_indicatorMarginBottom, 5);
//		typedArray.recycle();
		initViews();
	}

	@SuppressWarnings("deprecation")
	private void initViews() {
		viewPager = new ViewPager(context);
		LayoutParams viewPagerParams = new LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		viewPager.setLayoutParams(viewPagerParams);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setOnPageChangeListener(this);

		indicatorView = new LinearLayout(context);
		indicatorView.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams layoutParams = new LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, (int) containerHeight);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.bottomMargin = (int) indicatorMarginBottom;
		indicatorView.setGravity(Gravity.CENTER_VERTICAL
				| Gravity.CENTER_HORIZONTAL);
		indicatorView.setLayoutParams(layoutParams);
		if (indicatorBackgroud != 0) {
			indicatorView.setBackgroundDrawable(context.getResources()
					.getDrawable(indicatorBackgroud));
		}
		addView(viewPager);
		addView(indicatorView);
		// 自动轮播图片时间
		mHandler.sendEmptyMessageDelayed(0, 1500);
	}

	private PagerAdapter viewPagerAdapter = new PagerAdapter() {

		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		public int getCount() {
			return numberPages > 1 ? pageCount : 1;
		}

		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		public void finishUpdate(ViewGroup container) {
			if (numberPages == 1)
				return;
			int location = viewPager.getCurrentItem();
			if (location == pageCount - 1) {
				location = numberPages - 1;
				viewPager.setCurrentItem(location, false);
			} else if (location == 0) {
				location = numberPages;
				viewPager.setCurrentItem(location, false);
			}
		}

		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@SuppressWarnings("deprecation")
		public Object instantiateItem(ViewGroup container, int position) {
			position %= numberPages;
			ImageView imageView = null;
			imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setBackgroundDrawable(new BitmapDrawable(imageUrls
					.get(position)));
			imageView.setTag(position);
			imageView.setOnClickListener(new OnClickListener() {

				public void onClick(View view) {
					if (viewPagerClick != null) {
						viewPagerClick.viewPagerOnClick(view);
					}
				}
			});

			container.addView(imageView);
			return imageView;
		}
	};

	public void onPageScrollStateChanged(int arg0) {
	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	public void onPageSelected(int location) {
		setSelectPage(location % numberPages);
	}

	@SuppressWarnings("deprecation")
	private void createImageView(List<Bitmap> imageUrlList) {
		if (imageUrlList != null && imageUrlList.size() > 0) {
			View pointView;
			int position = 0;
			while (position < imageUrlList.size()) {
				pointView = new View(context);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						(int) (indicatorSize), (int) (indicatorSize));
				if (position != imageUrlList.size() - 1)
					params.rightMargin = (int) indicatorInterval;
				pointView.setLayoutParams(params);
				pointView.setBackgroundDrawable(context.getResources()
						.getDrawable(indicatorDrawable));
				pointView.setEnabled(false);
				indicatorView.addView(pointView);
				position++;
			}

			viewPagerAdapter.notifyDataSetChanged();
		}
	}

	private void setSelectPage(int position) {
		for (int index = 0; index < indicatorView.getChildCount(); index++) {
			if (position == index) {
				if (indicatorBigSize > 0) {
					LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) indicatorView
							.getChildAt(index).getLayoutParams();
					params.width = params.height = (int) indicatorBigSize;
				}
				indicatorView.getChildAt(index).setEnabled(true);
			} else {
				if (!indicatorView.getChildAt(index).isEnabled())
					continue;
				if (indicatorBigSize > 0) {
					LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) indicatorView
							.getChildAt(index).getLayoutParams();
					params.width = params.height = (int) indicatorSize;
				}
				indicatorView.getChildAt(index).setEnabled(false);
			}
		}
	}

	public void addImageUrls(List<Bitmap> imageUrls) {
		this.imageUrls.addAll(imageUrls);
		numberPages = this.imageUrls.size();
		pageCount = 2 * numberPages;
		createImageView(imageUrls);
	}

	public ViewPager getViewPager() {
		return viewPager;
	}

	public LinearLayout getIndicatorView() {
		return indicatorView;
	}

	public void setViewPagerClick(ViewPagerClick viewPagerClick) {
		this.viewPagerClick = viewPagerClick;
	}

	public interface ViewPagerClick {
		public void viewPagerOnClick(View view);
	}
}
