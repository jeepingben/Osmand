package net.osmand.plus.mapcontextmenu.other;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.osmand.plus.R;
import net.osmand.plus.activities.MapActivity;
import net.osmand.plus.mapcontextmenu.other.ObjectSelectionMenu.MenuMapObject;

import java.util.List;

public class ObjectSelectionMenuFragment extends Fragment implements AdapterView.OnItemClickListener {
	public static final String TAG = "ObjectSelectionMenuFragment";

	private ArrayAdapter<MenuMapObject> listAdapter;
	private ObjectSelectionMenu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null && getActivity() instanceof MapActivity) {
			menu = ObjectSelectionMenu.restoreMenu(savedInstanceState, (MapActivity) getActivity());
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.share_menu_fragment, container, false);

		ListView listView = (ListView) view.findViewById(R.id.list);
		listAdapter = createAdapter();
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		menu.getMapActivity().getContextMenu().setBaseFragmentVisibility(false);
	}

	@Override
	public void onStop() {
		super.onStop();
		menu.getMapActivity().getContextMenu().setBaseFragmentVisibility(true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		menu.saveMenu(outState);
	}

	public static void showInstance(ObjectSelectionMenu menu) {
		int slideInAnim = menu.getSlideInAnimation();
		int slideOutAnim = menu.getSlideOutAnimation();

		ObjectSelectionMenuFragment fragment = new ObjectSelectionMenuFragment();
		fragment.menu = menu;
		menu.getMapActivity().getSupportFragmentManager().beginTransaction()
				.setCustomAnimations(slideInAnim, slideOutAnim, slideInAnim, slideOutAnim)
				.add(R.id.fragmentContainer, fragment, TAG)
				.addToBackStack(TAG).commit();
	}

	private ArrayAdapter<MenuMapObject> createAdapter() {
		final List<MenuMapObject> items = menu.getObjects();
		return new ArrayAdapter<MenuMapObject>(menu.getMapActivity(), R.layout.share_list_item, items) {

			@SuppressLint("InflateParams")
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				View v = convertView;
				if (v == null) {
					v = menu.getMapActivity().getLayoutInflater().inflate(R.layout.share_list_item, null);
				}
				final MenuMapObject item = getItem(position);
				/*
				ImageView icon = (ImageView) v.findViewById(R.id.icon);
				icon.setImageDrawable(menu.getMapActivity().getMyApplication()
						.getIconsCache().getContentIcon(item.getIconResourceId()));
				TextView name = (TextView) v.findViewById(R.id.name);
				name.setText(getContext().getText(item.getTitleResourceId()));
				*/
				return v;
			}
		};
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//menu.share(listAdapter.getItem(position));
		dismissMenu();
	}

	public void dismissMenu() {
		if (menu.getMapActivity().getContextMenu().isVisible()) {
			menu.getMapActivity().getContextMenu().hide();
		} else {
			menu.getMapActivity().getSupportFragmentManager().popBackStack();
		}
	}
}
