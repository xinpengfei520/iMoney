package com.xpf.p2p;

import com.xpf.p2p.uetool.CustomView;

import java.util.ArrayList;
import java.util.List;

import me.ele.uetool.base.Element;
import me.ele.uetool.base.IAttrs;
import me.ele.uetool.base.item.Item;
import me.ele.uetool.base.item.TextItem;

/**
 * Created by x-sir on 2018/11/17 :)
 * Function:
 */
public class CustomAttribution implements IAttrs {

    @Override
    public List<Item> getAttrs(Element element) {
        List<Item> items = new ArrayList<>();
        if (element.getView() instanceof CustomView) {
            CustomView view = (CustomView) element.getView();
            items.add(new TextItem("More", view.moreAttribution));
        }
        return items;
    }
}
