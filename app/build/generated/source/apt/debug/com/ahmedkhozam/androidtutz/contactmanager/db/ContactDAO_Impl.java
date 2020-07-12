package com.ahmedkhozam.androidtutz.contactmanager.db;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.RxRoom;
import android.database.Cursor;
import com.ahmedkhozam.androidtutz.contactmanager.db.entity.Contact;
import io.reactivex.Flowable;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("unchecked")
public class ContactDAO_Impl implements ContactDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfContact;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfContact;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfContact;

  public ContactDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfContact = new EntityInsertionAdapter<Contact>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `contacts`(`contact_name`,`contact_email`,`contact_id`) VALUES (?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Contact value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        if (value.getEmail() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getEmail());
        }
        stmt.bindLong(3, value.getId());
      }
    };
    this.__deletionAdapterOfContact = new EntityDeletionOrUpdateAdapter<Contact>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `contacts` WHERE `contact_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Contact value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfContact = new EntityDeletionOrUpdateAdapter<Contact>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `contacts` SET `contact_name` = ?,`contact_email` = ?,`contact_id` = ? WHERE `contact_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Contact value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        if (value.getEmail() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getEmail());
        }
        stmt.bindLong(3, value.getId());
        stmt.bindLong(4, value.getId());
      }
    };
  }

  @Override
  public long addContact(Contact contact) {
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfContact.insertAndReturnId(contact);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteContact(Contact contact) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfContact.handle(contact);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateContact(Contact contact) {
    __db.beginTransaction();
    try {
      __updateAdapterOfContact.handle(contact);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Flowable<List<Contact>> getContacts() {
    final String _sql = "select * from contacts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return RxRoom.createFlowable(__db, new String[]{"contacts"}, new Callable<List<Contact>>() {
      @Override
      public List<Contact> call() throws Exception {
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("contact_name");
          final int _cursorIndexOfEmail = _cursor.getColumnIndexOrThrow("contact_email");
          final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("contact_id");
          final List<Contact> _result = new ArrayList<Contact>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Contact _item;
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Contact(_tmpId,_tmpName,_tmpEmail);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Contact getContact(long contactId) {
    final String _sql = "select * from contacts where contact_id ==?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, contactId);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("contact_name");
      final int _cursorIndexOfEmail = _cursor.getColumnIndexOrThrow("contact_email");
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("contact_id");
      final Contact _result;
      if(_cursor.moveToFirst()) {
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _result = new Contact(_tmpId,_tmpName,_tmpEmail);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
