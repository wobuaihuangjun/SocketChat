package com.example.huangzj.socketchat.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据持久化工具,可将List,对象以及基本数据类型持久化到文件中保存
 * 
 * @author lhd
 *
 */
public class PersistentDataUtil implements SharedPreferences {

	private String TAG = "PersistentDataUtil";

	private Context context;

	private SharedPreferences sharedPreferences;

	private static PersistentDataUtil persistentDataUtil;

	private PersistentDataUtil(Context context) {
		this.context = context;
		sharedPreferences = context.getSharedPreferences("sochetchat",
				Activity.MODE_PRIVATE);
	}

	public static PersistentDataUtil getInstance(Context context) {
		if (persistentDataUtil == null) {
			persistentDataUtil = new PersistentDataUtil(context);
		}
		return persistentDataUtil;
	}

	/**
	 * 保存json字符串
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putString(String key, String value) {
		boolean result = false;

		if (sharedPreferences != null) {
			Editor editor = sharedPreferences.edit();
			editor.putString(key, value);
			result = editor.commit();
		} else {
			Log.e(TAG, "sharedPreferences is null.");
			result = false;
		}

		return result;
	}

	public boolean putLong(String key, long value) {
		boolean result = false;

		if (sharedPreferences != null) {
			Editor editor = sharedPreferences.edit();
			editor.putLong(key, value);
			result = editor.commit();
		} else {
			Log.e(TAG, "sharedPreferences is null.");
			result = false;
		}

		return result;
	}

	public boolean putBoolean(String key, boolean value) {
		boolean result = false;

		if (sharedPreferences != null) {
			Editor editor = sharedPreferences.edit();
			editor.putBoolean(key, value);
			result = editor.commit();
		} else {
			Log.e(TAG, "sharedPreferences is null.");
			result = false;
		}

		return result;
	}
	
	/**
	 * 持久化对象
	 * 
	 * @param <T>
	 * @param key
	 * @param t
	 * @return
	 */
	public <T> boolean putObject(String key, T t) {
		boolean result = false;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(t);
			String objectDataString = Base64.encodeToString(baos.toByteArray(),
					Base64.DEFAULT);
			if (sharedPreferences != null) {
				Editor editor = this.edit();
				editor.putString(key, objectDataString);
				result = editor.commit();
			} else {
				Log.e(TAG, "sharedPreferences is null.");
				result = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 获取持久化对象
	 * 
	 * @param key
	 * @param cls
	 * @return
	 */
	public <T> T getObject(String key, Class<T> cls) {
		String data = sharedPreferences.getString(key, null);
		T t = null;
		if (data == null)
			return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(
				data, Base64.DEFAULT));
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bais);
			t = (T) ois.readObject();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bais.close();
				if (ois != null)
					ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return t;
	}

	/**
	 * 持久化列表List
	 * 
	 * @param key
	 * @param list
	 * @return
	 */
	public <T> boolean putList(String key, List<T> list) {
		return putObject(key, list);
	}

	/**
	 * 获取持久化列表List
	 * 
	 * @param key
	 * @param t
	 * @return
	 */
	public <T> List<T> getList(String key, Class<T> t) {
		String data = sharedPreferences.getString(key, null);
		List<T> l = null;
		if (data == null)
			return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(
				data, Base64.DEFAULT));
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bais);
			l = (List<T>) ois.readObject();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bais.close();
				if (ois != null)
					ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return l;
	}

	@Override
	public Map<String, ?> getAll() {
		return sharedPreferences.getAll();
	}

	@Override
	public String getString(String key, String defValue) {
		return sharedPreferences.getString(key, defValue);
	}

	@SuppressLint("NewApi")
	@Override
	public Set<String> getStringSet(String key, Set<String> defValues) {
		return sharedPreferences.getStringSet(key, defValues);
	}

	@Override
	public int getInt(String key, int defValue) {
		return sharedPreferences.getInt(key, defValue);
	}

	@Override
	public long getLong(String key, long defValue) {
		return sharedPreferences.getLong(key, defValue);
	}

	@Override
	public float getFloat(String key, float defValue) {
		return sharedPreferences.getFloat(key, defValue);
	}

	@Override
	public boolean getBoolean(String key, boolean defValue) {
		return sharedPreferences.getBoolean(key, defValue);
	}

	@Override
	public boolean contains(String key) {
		return sharedPreferences.contains(key);
	}

	@Override
	public Editor edit() {
		return sharedPreferences.edit();
	}

	@Override
	public void registerOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
	}

	@Override
	public void unregisterOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
	}
}
