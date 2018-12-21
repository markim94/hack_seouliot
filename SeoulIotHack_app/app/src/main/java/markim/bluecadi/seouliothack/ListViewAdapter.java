package markim.bluecadi.seouliothack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    public ListViewAdapter(){

    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView txtFloor = (TextView) convertView.findViewById(R.id.txtFloor) ;
        TextView txtPerson = (TextView) convertView.findViewById(R.id.txtPerson) ;
        TextView txtState = (TextView) convertView.findViewById(R.id.txtState) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        // 비콘에서 정보 받아서 데이터 반영
        txtFloor.setText(listViewItem.getFloor());
        txtPerson.setText(listViewItem.getPerson());
        txtState.setText(listViewItem.getState());


        return convertView;
    }

    // 비콘에서 감지된 기기수를 person으로 전달
    public void addItem(String floor, String state, String person){

        ListViewItem item = new ListViewItem();

        item.setFloor(floor);
        item.setPerson(person);
        if(state.equals("1")){
            item.setState("화재발생");
        } else{
            item.setState(" ");
        }

        listViewItemList.add(item);

    }





}

