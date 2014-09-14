package xpages;

import java.util.ArrayList;
import java.util.List;

import com.ibm.xsp.extlib.component.picker.data.IPickerEntry;
import com.ibm.xsp.extlib.component.picker.data.IPickerOptions;
import com.ibm.xsp.extlib.component.picker.data.IPickerResult;
import com.ibm.xsp.extlib.component.picker.data.IValuePickerData;
import com.ibm.xsp.extlib.component.picker.data.SimplePickerResult;

public class SQLPicker implements IValuePickerData {
	
	public SQLPicker() {
	}
	
	public String[] getSourceLabels() {
		return null;
	}

	public boolean hasCapability(int capability) {
		if(capability==CAPABILITY_EXTRAATTRIBUTES) {
			return false;
		}
		return false;
	}

	public List<IPickerEntry> loadEntries(Object[] ids, String[] attributes) {
		List<IPickerEntry> entries = new ArrayList<IPickerEntry>();
		if(ids!=null) {
			for(int i=0; i<ids.length; i++) {
				String id = ids[i].toString();
				entries.add(new SimplePickerResult.Entry(id,null));			
			}
		}
		return entries;
	}

	public IPickerResult readEntries(IPickerOptions options) {
		int start = options.getStart();
		int count = options.getCount();
		List<IPickerEntry> entries = new ArrayList<IPickerEntry>();
		for(int i=start; count>0 && i<REQUESTS.length; i++, count--) {
			String r = REQUESTS[i];
			entries.add(new SimplePickerResult.Entry(r,null));
		}
		return new SimplePickerResult(entries,-1);
	}
	
	private static final String[] REQUESTS = {
		"select * from AllStates",

        "select * from AllStates a order by a.Key",
        "select * from AllStates a order by a.Key desc",
        "select * from AllStates a order by a.Name",
        "select * from AllStates a order by a.Name desc",
        "select * from AllStates a where a.Key='TX'",
        "select * from AllStates a where a.Key='INV'",
        "select * from AllStates a where a.Key like 'T%'",
        "select * from AllStates a where a.Name='MASSACHUSETTS'",
        "select * from AllStates a where a.Name like 'M%'",

        "select a.fldtext, b._rowid, b.fldtext2 FROM alltypes a left outer join alltypesmultitext b on a._ID = b._ID",
        "select a.fldtext, b._rowid, b.fldtext2 FROM alltypes a, alltypesmultitext b where a._ID = b._ID",

        "select * from AllContacts",
        "select * from AllContactsByState",
		"select a.FirstName, a.LastName from AllContacts a",
        "select a.FirstName, a.LastName from AllContacts a where a.FirstName='AlexisWrong'",
        "select a.FirstName, a.LastName from AllContacts a where a.FirstName='Alexis'",
        "select a.FirstName, a.LastName from AllContacts a where a.FirstName='Alexis' and a.LastName='Rich'",
        "select a.FirstName, a.LastName from AllContacts a where a.FirstName='Alexis' and a.LastName='RichWrong'",
		"select a.FirstName, a.LastName, a.EMail from AllContacts a order by a.LastName",
		"select a.FirstName, a.LastName, a.EMail from AllContacts a order by a.EMail",
		"select a.FirstName, a.LastName, a.EMail from AllContacts a where a.EMail='bjeanser@acme.com' order by a.EMail",
		"select a.FirstName, a.LastName, a.EMail from AllContacts a where a.FirstName='Albert' order by a.EMail",
		"select a.FirstName, count(*) from AllContacts a group by a.FirstName",
		"select a.FirstName, a.LastName, b.Key, b.Name from AllContacts a, AllStates b where a.State=b.Key",
		"select a.FirstName, a.LastName, a.State, b.Key, b.Name from AllContacts a left outer join AllStates b on a.State=b.Key",
		"select a.FirstName, a.LastName, b.Key, b.Name from AllContacts a left outer join AllStates b on a.State=b.Key order by b.Name",
		"select a.FirstName, a.LastName, a.EMail from AllContacts a where a.EMail='bjeanser@acme.com' union select b.FirstName, b.LastName, b.EMail from AllContacts b where b.FirstName='Albert'",
	};
}
