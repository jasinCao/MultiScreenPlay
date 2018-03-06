package com.simple.autolayout;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simple.autolayout.attr.Attrs;
import com.simple.autolayout.attr.AutoAttr;
import com.simple.autolayout.attr.HeightAttr;
import com.simple.autolayout.attr.MarginBottomAttr;
import com.simple.autolayout.attr.MarginLeftAttr;
import com.simple.autolayout.attr.MarginRightAttr;
import com.simple.autolayout.attr.MarginTopAttr;
import com.simple.autolayout.attr.MaxHeightAttr;
import com.simple.autolayout.attr.MaxWidthAttr;
import com.simple.autolayout.attr.MinHeightAttr;
import com.simple.autolayout.attr.MinWidthAttr;
import com.simple.autolayout.attr.PaddingBottomAttr;
import com.simple.autolayout.attr.PaddingLeftAttr;
import com.simple.autolayout.attr.PaddingRightAttr;
import com.simple.autolayout.attr.PaddingTopAttr;
import com.simple.autolayout.attr.TextSizeAttr;
import com.simple.autolayout.attr.WidthAttr;
import com.simple.log.Slog;
import com.simple.multiscreenplay.R;

public class AutoLayoutInfo {
	private List<AutoAttr> autoAttrs = new ArrayList<AutoAttr>();

	public void addAttr(AutoAttr autoAttr) {
		autoAttrs.add(autoAttr);
	}

	public void fillAttrs(View view) {
		if (view.getTag(R.id.id_tag_fix_complate) != null) {
			Slog.d("fillAttrs",
					"fillAttrs =============>"
							+ view.getTag(R.id.id_tag_fix_complate));
		} else {
			for (AutoAttr autoAttr : autoAttrs) {
				autoAttr.apply(view);
			}
			view.setTag(R.id.id_tag_fix_complate, 1);
		}

	}

	public static AutoLayoutInfo getAttrFromView(View view, int attrs, int base) {
		ViewGroup.LayoutParams params = view.getLayoutParams();
		if (params == null)
			return null;
		AutoLayoutInfo autoLayoutInfo = new AutoLayoutInfo();

		// width & height
		if ((attrs & Attrs.WIDTH) != 0 && params.width > 0) {
			autoLayoutInfo.addAttr(WidthAttr.generate(params.width, base));
		}

		if ((attrs & Attrs.HEIGHT) != 0 && params.height > 0) {
			Slog.d("autoinfo", "params.height=============>" + params.height);
			Slog.d("autoinfo",
					"=============>" + HeightAttr.generate(params.height, base));
			autoLayoutInfo.addAttr(HeightAttr.generate(params.height, base));
		}

		// margin
		if (params instanceof ViewGroup.MarginLayoutParams) {
			if ((attrs & Attrs.MARGIN) != 0) {
				autoLayoutInfo.addAttr(MarginLeftAttr.generate(
						((ViewGroup.MarginLayoutParams) params).leftMargin,
						base));
				autoLayoutInfo
						.addAttr(MarginTopAttr
								.generate(
										((ViewGroup.MarginLayoutParams) params).topMargin,
										base));
				autoLayoutInfo.addAttr(MarginRightAttr.generate(
						((ViewGroup.MarginLayoutParams) params).rightMargin,
						base));
				autoLayoutInfo.addAttr(MarginBottomAttr.generate(
						((ViewGroup.MarginLayoutParams) params).bottomMargin,
						base));
			}
			if ((attrs & Attrs.MARGIN_LEFT) != 0) {
				autoLayoutInfo.addAttr(MarginLeftAttr.generate(
						((ViewGroup.MarginLayoutParams) params).leftMargin,
						base));
			}
			if ((attrs & Attrs.MARGIN_TOP) != 0) {
				autoLayoutInfo
						.addAttr(MarginTopAttr
								.generate(
										((ViewGroup.MarginLayoutParams) params).topMargin,
										base));
			}
			if ((attrs & Attrs.MARGIN_RIGHT) != 0) {
				autoLayoutInfo.addAttr(MarginRightAttr.generate(
						((ViewGroup.MarginLayoutParams) params).rightMargin,
						base));
			}
			if ((attrs & Attrs.MARGIN_BOTTOM) != 0) {
				autoLayoutInfo.addAttr(MarginBottomAttr.generate(
						((ViewGroup.MarginLayoutParams) params).bottomMargin,
						base));
			}
		}

		// padding
		if ((attrs & Attrs.PADDING) != 0) {
			autoLayoutInfo.addAttr(PaddingLeftAttr.generate(
					view.getPaddingLeft(), base));
			autoLayoutInfo.addAttr(PaddingTopAttr.generate(
					view.getPaddingTop(), base));
			autoLayoutInfo.addAttr(PaddingRightAttr.generate(
					view.getPaddingRight(), base));
			autoLayoutInfo.addAttr(PaddingBottomAttr.generate(
					view.getPaddingBottom(), base));
		}
		if ((attrs & Attrs.PADDING_LEFT) != 0) {
			autoLayoutInfo.addAttr(MarginLeftAttr.generate(
					view.getPaddingLeft(), base));
		}
		if ((attrs & Attrs.PADDING_TOP) != 0) {
			autoLayoutInfo.addAttr(MarginTopAttr.generate(view.getPaddingTop(),
					base));
		}
		if ((attrs & Attrs.PADDING_RIGHT) != 0) {
			autoLayoutInfo.addAttr(MarginRightAttr.generate(
					view.getPaddingRight(), base));
		}
		if ((attrs & Attrs.PADDING_BOTTOM) != 0) {
			autoLayoutInfo.addAttr(MarginBottomAttr.generate(
					view.getPaddingBottom(), base));
		}

		// minWidth ,maxWidth , minHeight , maxHeight
		if ((attrs & Attrs.MIN_WIDTH) != 0) {
			autoLayoutInfo.addAttr(MinWidthAttr.generate(
					MinWidthAttr.getMinWidth(view), base));
		}
		if ((attrs & Attrs.MAX_WIDTH) != 0) {
			autoLayoutInfo.addAttr(MaxWidthAttr.generate(
					MaxWidthAttr.getMaxWidth(view), base));
		}
		if ((attrs & Attrs.MIN_HEIGHT) != 0) {
			autoLayoutInfo.addAttr(MinHeightAttr.generate(
					MinHeightAttr.getMinHeight(view), base));
		}
		if ((attrs & Attrs.MAX_HEIGHT) != 0) {
			autoLayoutInfo.addAttr(MaxHeightAttr.generate(
					MaxHeightAttr.getMaxHeight(view), base));
		}

		// textsize

		if (view instanceof TextView) {
			if ((attrs & Attrs.TEXTSIZE) != 0) {
				autoLayoutInfo.addAttr(TextSizeAttr.generate(
						(int) ((TextView) view).getTextSize(), base));
			}
		}
		return autoLayoutInfo;
	}

	@Override
	public String toString() {
		return "AutoLayoutInfo{" + "autoAttrs=" + autoAttrs + '}';
	}
}