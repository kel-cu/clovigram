/*
 * This is the source code of Telegram for Android v. 5.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */

package art.clovi.preferences;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.util.Util;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.ChatEditActivity;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ListView.AdapterWithDiffUtils;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.Switch;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;

import java.util.ArrayList;

import art.clovi.CloviConfig;
import art.clovi.ui.MD3AdapterWithDiffUtils;
import art.clovi.util.GlyphsUtils;

public class MainPreferencesActivity extends BaseFragment {

    FrameLayout contentView;

    RecyclerListView listView;
    LinearLayoutManager layoutManager;
    MainPreferencesActivity.Adapter adapter;

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString(R.string.CloviSettings));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        contentView = new FrameLayout(context);
        contentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));

        listView = new RecyclerListView(context);
        listView.setLayoutManager(layoutManager = new LinearLayoutManager(context));
        listView.setAdapter(adapter = new MainPreferencesActivity.Adapter());
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setDurations(350);
        itemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        itemAnimator.setDelayAnimations(false);
        itemAnimator.setSupportsChangeAnimations(false);
        listView.setItemAnimator(itemAnimator);
        contentView.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setOnItemClickListener((view, position, x, y) -> {
            if (view == null || position < 0 || position >= items.size()) {
                return;
            }
            final MainPreferencesActivity.Item item = items.get(position);
            if(item.flags == -1){
              AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
              builder.setTitle("test");
              builder.setMessage("Glyph command has been sent");
              GlyphsUtils.sendTest();
            } else if (item.viewType == VIEW_TYPE_SWITCH || item.viewType == VIEW_TYPE_SWITCH2 || item.viewType == VIEW_TYPE_CHECKBOX) {
                CloviConfig.setValue(item.flags);
                if(item.needRestart) Toast.makeText(context, LocaleController.getString(R.string.NeedRestartDescription), Toast.LENGTH_LONG).show();
                updateValues();
            }
        });

        fragmentView = contentView;

        updateItems();

        return fragmentView;
    }

    @Override
    public void onBecomeFullyVisible() {
        super.onBecomeFullyVisible();
    }

    @Override
    public void onBecomeFullyHidden() {
        super.onBecomeFullyHidden();
    }

    private Utilities.Callback<Boolean> onPowerAppliedChange = applied -> updateValues();

    private boolean[] expanded = new boolean[3];

    public void scrollToType(int type) {
        for (int i = 0; i < items.size(); i++) {
            MainPreferencesActivity.Item item = items.get(i);
            if (item.type == type) {
                highlightRow(i);
                break;
            }
        }
    }

    public void scrollToFlags(int flags) {
        for (int i = 0; i < items.size(); i++) {
            MainPreferencesActivity.Item item = items.get(i);
            if (item.flags == flags) {
                highlightRow(i);
                break;
            }
        }
    }

    private void highlightRow(int index) {
        RecyclerListView.IntReturnCallback callback = () -> {
            layoutManager.scrollToPositionWithOffset(index, AndroidUtilities.dp(60));
            return index;
        };
        listView.highlightRow(callback);
    }

    private ArrayList<MainPreferencesActivity.Item> oldItems = new ArrayList<>();
    private ArrayList<MainPreferencesActivity.Item> items = new ArrayList<>();

    private void updateItems() {
        oldItems.clear();
        oldItems.addAll(items);

        items.clear();
        // -=-=-=- Base
        items.add(MainPreferencesActivity.Item.asHeader(LocaleController.getString(R.string.Basic)));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.msg_camera, LocaleController.getString(R.string.StartWithBackCamera), 3));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.msg_photo_text_regular, LocaleController.getString(R.string.UseSystemFont), 11, true));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.msg_emoji_cat, LocaleController.getString(R.string.UseSystemEmoji), 12, true));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.left_status_profile, LocaleController.getString(R.string.ShowProfileButton), 14));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.msg_stats, LocaleController.getString(R.string.AddPostStatsButton), 17));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.msg_settings_ny, LocaleController.getString(R.string.ShowSnowOnProfile), 19, false));
        items.add(MainPreferencesActivity.Item.asInfo(LocaleController.getString(R.string.NeedRestartDescription)));
        // -=-=-=- Replaceable
        items.add(MainPreferencesActivity.Item.asHeader(LocaleController.getString(R.string.Replaceable)));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.menu_tag_rename, LocaleController.getString(R.string.ReplaceTitleWithName), 1, true));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.floating_pencil, LocaleController.getString(R.string.ChangeEditedToPen), 16));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.premium_colors, LocaleController.getString(R.string.UsePremiumBackgroundOnDrawer), 18, true));
        items.add(Item.asInfo(""));
        // -=-=-=- Disabled items
        items.add(MainPreferencesActivity.Item.asHeader(LocaleController.getString(R.string.DisabledItems)));
        if(AndroidUtilities.needRealIsTablet()) items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.device_tablet_android, LocaleController.getString(R.string.DisableTabletMode), 8, true));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.msg_secret, LocaleController.getString(R.string.HidePhoneNumber), 0));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.menu_cover_stories, LocaleController.getString(R.string.HideStories), 7));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.msg_allowspeak, LocaleController.getString(R.string.HideDeveloperSite), 10));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.fingerprint, LocaleController.getString(R.string.DisableDoubleTapReactions), 13));
        items.add(Item.asInfo(""));
        // -=-=-=- MD3
        items.add(MainPreferencesActivity.Item.asHeader(LocaleController.getString(R.string.MaterialDesign3)));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.msg_list, LocaleController.getString(R.string.useMD3ListDesign), 9));
        items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.mini_checklist_done, LocaleController.getString(R.string.useMD3ComponentsDesign), 15));
        items.add(Item.asInfo(""));
        // -=-=-=- Nothing Glyph
        if(!GlyphsUtils.isSupportedDevice()){
            if(Util.SDK_INT < 36){
                items.add(MainPreferencesActivity.Item.asInfo(LocaleController.getString(R.string.NoSupportedAndroidNothing)));
            } else items.add(MainPreferencesActivity.Item.asInfo(LocaleController.getString(R.string.NoNothing)));
        } else {
            items.add(MainPreferencesActivity.Item.asHeader(LocaleController.getString(R.string.NothingGlyph)));
            items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.msg_noise_on, LocaleController.getString(R.string.EnableGlyphInterface), 4));
            items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.ab_progress, LocaleController.getString(R.string.AllowShowProgressGlyph), 5));
            items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.msg_videocall, LocaleController.getString(R.string.AllowShowRecordGlyph), 6));
            items.add(MainPreferencesActivity.Item.asInfo(String.format("%s: %s", LocaleController.getString(R.string.NothingSeries), GlyphsUtils.getDeviceName())));
            if(ApplicationLoader.isBetaBuild()) items.add(MainPreferencesActivity.Item.asSwitch(R.drawable.msg_photo_text_framed2,"test toggle", -1));
        }

//        items.add(MainPreferencesActivity.Item.asCheckbox(R.drawable.msg_map, LocaleController.getString(R.string.UseYandexMaps), 2, true));

        adapter.setItems(oldItems, items);
    }

    private void updateValues() {
        if (listView == null) {
            return;
        }
        for (int i = 0; i < listView.getChildCount(); ++i) {
            View child = listView.getChildAt(i);
            if (child == null) {
                continue;
            }
            int position = listView.getChildAdapterPosition(child);
            if (position < 0 || position >= items.size()) {
                continue;
            }
            MainPreferencesActivity.Item item = items.get(position);
            if (item.viewType == VIEW_TYPE_SWITCH || item.viewType == VIEW_TYPE_CHECKBOX) {
                ((MainPreferencesActivity.SwitchCell) child).update(item);
            }
        }
    }

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_SLIDER = 1;
    private static final int VIEW_TYPE_INFO = 2;
    private static final int VIEW_TYPE_SWITCH = 3;
    private static final int VIEW_TYPE_CHECKBOX = 4;
    private static final int VIEW_TYPE_SWITCH2 = 5;

    public static final int SWITCH_TYPE_SMOOTH_TRANSITIONS = 1;

    private class Adapter extends MD3AdapterWithDiffUtils {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final Context context = parent.getContext();
            View view = null;
            if (viewType == VIEW_TYPE_HEADER) {
                view = new HeaderCell(context);
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else if (viewType == VIEW_TYPE_INFO) {
                view = new TextInfoPrivacyCell(context) {
                    @Override
                    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
                        super.onInitializeAccessibilityNodeInfo(info);

                        info.setEnabled(true);
                    }

                    @Override
                    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
                        super.onPopulateAccessibilityEvent(event);

                        event.setContentDescription(getTextView().getText());
                        setContentDescription(getTextView().getText());
                    }
                };
            } else if (viewType == VIEW_TYPE_SWITCH || viewType == VIEW_TYPE_CHECKBOX) {
                view = new MainPreferencesActivity.SwitchCell(context);
            } else if (viewType == VIEW_TYPE_SWITCH2) {
                view = new TextCell(context, 23, false, true, null);
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (position < 0 || position >= items.size()) {
                return;
            }

            final MainPreferencesActivity.Item item = items.get(position);
            final int viewType = holder.getItemViewType();
            if (viewType == VIEW_TYPE_HEADER) {
                HeaderCell headerCell = (HeaderCell) holder.itemView;
                headerCell.setText(item.text);
            } else if (viewType == VIEW_TYPE_INFO) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) holder.itemView;
                if (TextUtils.isEmpty(item.text)) {
                    textInfoPrivacyCell.setFixedSize(12);
                } else {
                    textInfoPrivacyCell.setFixedSize(0);
                }
                textInfoPrivacyCell.setText(item.text);
                textInfoPrivacyCell.setContentDescription(item.text);
                boolean top = position > 0 && items.get(position - 1).viewType != VIEW_TYPE_INFO;
                boolean bottom = position + 1 < items.size() && items.get(position + 1).viewType != VIEW_TYPE_INFO;
                if (top && bottom) {
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(getContext(), R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                } else if (top) {
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(getContext(), R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                } else if (bottom) {
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(getContext(), R.drawable.greydivider_top, Theme.key_windowBackgroundGrayShadow));
                } else {
                    textInfoPrivacyCell.setBackground(null);
                }
            } else if (viewType == VIEW_TYPE_SWITCH || viewType == VIEW_TYPE_CHECKBOX) {
                final boolean divider = position + 1 < items.size() && items.get(position + 1).viewType != VIEW_TYPE_INFO;
                MainPreferencesActivity.SwitchCell switchCell = (MainPreferencesActivity.SwitchCell) holder.itemView;
                switchCell.set(item, divider);
            } else if (viewType == VIEW_TYPE_SWITCH2) {
                TextCell textCell = (TextCell) holder.itemView;
                if (item.type == SWITCH_TYPE_SMOOTH_TRANSITIONS) {
                    SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                    boolean animations = preferences.getBoolean("view_animations", true);
                    textCell.setTextAndCheck(item.text, animations, false);
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position < 0 || position >= items.size()) {
                return VIEW_TYPE_INFO;
            }
            return items.get(position).viewType;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            return holder.getItemViewType() == VIEW_TYPE_CHECKBOX || holder.getItemViewType() == VIEW_TYPE_SWITCH || holder.getItemViewType() == VIEW_TYPE_SWITCH2;
        }
    }

    private class SwitchCell extends FrameLayout {

        private ImageView imageView;
        private LinearLayout textViewLayout;
        private TextView textView;
        private AnimatedTextView countTextView;
        private ImageView arrowView;
        private Switch switchView;
        private CheckBox2 checkBoxView;

        private boolean needDivider, needLine;

        public SwitchCell(Context context) {
            super(context);

            setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);
            setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));

            imageView = new ImageView(context);
            imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon), PorterDuff.Mode.MULTIPLY));
            imageView.setVisibility(View.GONE);
            addView(imageView, LayoutHelper.createFrame(24, 24, Gravity.CENTER_VERTICAL | (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT), 20, 0, 20, 0));

            textView = new androidx.appcompat.widget.AppCompatTextView(context) {
                @Override
                protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                    if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
                        widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(52), MeasureSpec.AT_MOST);
                    }
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                }
            };
            textView.setLines(1);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            textView.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
            textView.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);

            countTextView = new AnimatedTextView(context, false, true, true);
            countTextView.setAnimationProperties(.35f, 0, 200, CubicBezierInterpolator.EASE_OUT_QUINT);
            countTextView.setTypeface(AndroidUtilities.bold());
            countTextView.setTextSize(dp(14));
            countTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            countTextView.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);

            arrowView = new ImageView(context);
            arrowView.setVisibility(GONE);
            arrowView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText), PorterDuff.Mode.MULTIPLY));
            arrowView.setImageResource(R.drawable.arrow_more);

            textViewLayout = new LinearLayout(context);
            textViewLayout.setOrientation(LinearLayout.HORIZONTAL);
            textViewLayout.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
            if (LocaleController.isRTL) {
                textViewLayout.addView(arrowView, LayoutHelper.createLinear(16, 16, 0, Gravity.CENTER_VERTICAL, 0, 0, 6, 0));
                textViewLayout.addView(countTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, Gravity.CENTER_VERTICAL, 0, 0, 6, 0));
                textViewLayout.addView(textView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
            } else {
                textViewLayout.addView(textView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                textViewLayout.addView(countTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, Gravity.CENTER_VERTICAL, 6, 0, 0, 0));
                textViewLayout.addView(arrowView, LayoutHelper.createLinear(16, 16, 0, Gravity.CENTER_VERTICAL, 2, 0, 0, 0));
            }
            addView(textViewLayout, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL | (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT), 64, 0, 8, 0));

            switchView = new Switch(context);
            switchView.setVisibility(GONE);
            switchView.setColors(Theme.key_switchTrack, Theme.key_switchTrackChecked, Theme.key_windowBackgroundWhite, Theme.key_windowBackgroundWhite);
            switchView.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
            addView(switchView, LayoutHelper.createFrame(37, 50, Gravity.CENTER_VERTICAL | (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT), 19, 0, 19, 0));

            checkBoxView = new CheckBox2(context, 21);
            checkBoxView.setColor(Theme.key_radioBackgroundChecked, Theme.key_checkboxDisabled, Theme.key_checkboxCheck);
            checkBoxView.setDrawUnchecked(true);
            checkBoxView.setChecked(true, false);
            checkBoxView.setDrawBackgroundAsArc(10);
            checkBoxView.setVisibility(GONE);
            checkBoxView.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
            addView(checkBoxView, LayoutHelper.createFrame(21, 21, Gravity.CENTER_VERTICAL | (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT), LocaleController.isRTL ? 0 : 64, 0, LocaleController.isRTL ? 64 : 0, 0));

            setFocusable(true);
        }


        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(
                    MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(dp(50), MeasureSpec.EXACTLY)
            );
        }

        public void set(MainPreferencesActivity.Item item, boolean divider) {
            if (item.viewType == VIEW_TYPE_SWITCH) {
                checkBoxView.setVisibility(GONE);
                imageView.setVisibility(VISIBLE);
                imageView.setImageResource(item.iconResId);
                textView.setText(item.text);
                countTextView.setVisibility(GONE);
                arrowView.setVisibility(GONE);
                switchView.setVisibility(VISIBLE);
                switchView.setChecked(CloviConfig.getValue(item.flags), false);
            } else {
                checkBoxView.setVisibility(VISIBLE);
                checkBoxView.setChecked(CloviConfig.getValue(item.flags), false);
                imageView.setImageResource(item.iconResId);
                imageView.setVisibility(VISIBLE);
                switchView.setVisibility(GONE);
                countTextView.setVisibility(GONE);
                arrowView.setVisibility(GONE);
                textView.setText(item.text);
                textView.setTranslationX(dp(41) * (LocaleController.isRTL ? -2.2f : 1));
            }
            containing = false;
            needLine = false;

            ((MarginLayoutParams) textViewLayout.getLayoutParams()).rightMargin = AndroidUtilities.dp(16);

            setWillNotDraw(!((needDivider = divider) || needLine));
        }

        public void update(MainPreferencesActivity.Item item) {
            checkBoxView.setChecked(CloviConfig.getValue(item.flags), true);
            switchView.setChecked(CloviConfig.getValue(item.flags), true);
        }

        private boolean containing;
        private int enabled, all;

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (LocaleController.isRTL) {
                if (needLine) {
                    float x = dp(19 + 37 + 19);
                    canvas.drawRect(x - dp(0.66f), (getMeasuredHeight() - dp(20)) / 2f, x, (getMeasuredHeight() + dp(20)) / 2f, Theme.dividerPaint);
                }
                if (needDivider) {
                    canvas.drawLine(getMeasuredWidth() - dp(64) + (textView.getTranslationX() < 0 ? dp(-32) : 0), getMeasuredHeight() - 1, 0, getMeasuredHeight() - 1, Theme.dividerPaint);
                }
            } else {
                if (needLine) {
                    float x = getMeasuredWidth() - dp(19 + 37 + 19);
                    canvas.drawRect(x - dp(0.66f), (getMeasuredHeight() - dp(20)) / 2f, x, (getMeasuredHeight() + dp(20)) / 2f, Theme.dividerPaint);
                }
                if (needDivider) {
                    canvas.drawLine(dp(64) + textView.getTranslationX(), getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
                }
            }
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            info.setClassName(checkBoxView.getVisibility() == View.VISIBLE ? "android.widget.CheckBox" : "android.widget.Switch");
            info.setCheckable(true);
            info.setEnabled(true);
            if (checkBoxView.getVisibility() == View.VISIBLE) {
                info.setChecked(checkBoxView.isChecked());
            } else {
                info.setChecked(switchView.isChecked());
            }
            StringBuilder sb = new StringBuilder();
            sb.append(textView.getText());
            if (containing) {
                sb.append('\n');
                sb.append(LocaleController.formatString("Of", R.string.Of, enabled, all));
            }
            info.setContentDescription(sb);
        }
    }
    private static class Item extends AdapterWithDiffUtils.Item {
        public CharSequence text;
        public int iconResId;
        public int flags;
        public int type;
        public boolean needRestart;

        private Item(int viewType, CharSequence text, int iconResId, int flags, int type) {
            this(viewType, text, iconResId, flags, type, false);
        }
        private Item(int viewType, CharSequence text, int iconResId, int flags, int type, boolean needRestart) {
            super(viewType, false);
            this.text = text;
            this.iconResId = iconResId;
            this.flags = flags;
            this.type = type;
            this.needRestart = needRestart;
        }

        public static MainPreferencesActivity.Item asHeader(CharSequence text) {
            return new MainPreferencesActivity.Item(VIEW_TYPE_HEADER, text, 0, 0, 0);
        }
        public static MainPreferencesActivity.Item asSlider() {
            return new MainPreferencesActivity.Item(VIEW_TYPE_SLIDER, null, 0, 0, 0);
        }
        public static MainPreferencesActivity.Item asInfo(CharSequence text) {
            return new MainPreferencesActivity.Item(VIEW_TYPE_INFO, text, 0, 0, 0);
        }
        public static MainPreferencesActivity.Item asSwitch(int iconResId, CharSequence text, int flags) {
            return new MainPreferencesActivity.Item(VIEW_TYPE_SWITCH, text, iconResId, flags, 0);
        }
        public static MainPreferencesActivity.Item asSwitch(int iconResId, CharSequence text, int flags, boolean needRestart) {
            return new MainPreferencesActivity.Item(VIEW_TYPE_SWITCH, text, iconResId, flags, 0, needRestart);
        }
        public static MainPreferencesActivity.Item asCheckbox(int icon, CharSequence text, int flags) {
            return new MainPreferencesActivity.Item(VIEW_TYPE_CHECKBOX, text, icon, flags, 0);
        }
        public static MainPreferencesActivity.Item asCheckbox(int icon, CharSequence text, int flags, boolean needRestart) {
            return new MainPreferencesActivity.Item(VIEW_TYPE_CHECKBOX, text, icon, flags, 0, needRestart);
        }
        public static MainPreferencesActivity.Item asSwitch(CharSequence text, int type) {
            return new MainPreferencesActivity.Item(VIEW_TYPE_SWITCH2, text, 0, type, 0);
        }

        public int getFlagsCount() {
            return Integer.bitCount(flags);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof MainPreferencesActivity.Item)) {
                return false;
            }
            MainPreferencesActivity.Item item = (MainPreferencesActivity.Item) o;
            if (item.viewType != viewType) {
                return false;
            }
            if (viewType == VIEW_TYPE_SWITCH) {
                if (item.iconResId != iconResId) {
                    return false;
                }
            }
            if (viewType == VIEW_TYPE_SWITCH2) {
                if (item.type != type) {
                    return false;
                }
            }
            if (viewType == VIEW_TYPE_SWITCH) {
                if (item.flags != flags) {
                    return false;
                }
            }
            if (viewType == VIEW_TYPE_HEADER || viewType == VIEW_TYPE_INFO || viewType == VIEW_TYPE_SWITCH || viewType == VIEW_TYPE_CHECKBOX || viewType == VIEW_TYPE_SWITCH2) {
                if (!TextUtils.equals(item.text, text)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        AnimatedEmojiDrawable.updateAll();
        Theme.reloadWallpaper(true);
    }
}
