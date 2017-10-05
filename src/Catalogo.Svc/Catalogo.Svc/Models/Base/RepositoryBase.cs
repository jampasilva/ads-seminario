using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;

namespace Catalogo.Svc.Models.Base
{
    public abstract class RepositoryBase<T> : Object where T : IEntity
    {
        private static readonly ConcurrentDictionary<Type, List<T>> Cache;

        private readonly Lazy<List<T>> _table;
        private readonly Object _syncTableAccess = new object();

        static RepositoryBase()
        {
            Cache = new ConcurrentDictionary<Type, List<T>>();
        }
        protected RepositoryBase()
        {
            _table = new Lazy<List<T>>(() => Cache.GetOrAdd(typeof(T), type => CreatePopulatedList()));
        }

        private List<T> Table => _table.Value;

        public T Get(int id)
        {
            lock(_syncTableAccess)
            {
                return Table.FirstOrDefault(e => e.ID == id);
            }
        }

        public IEnumerable<T> GetAll()
        {
            lock(_syncTableAccess)
            {
                return Table;
            }
        }

        public bool Insert(T entity)
        {
            if(entity == null)
            {
                throw new ArgumentNullException(nameof(entity));
            }

            lock(_syncTableAccess)
            {
                if(Table.Count + 1 > GetMaxTableRowsCount())
                {
                    return false;
                }

                var maxID = Table.Max(e => e.ID);
                entity.ID = maxID + 1;

                Table.Add(entity);

                return true;
            }
        }
        public bool Update(T entity)
        {
            if(entity == null)
            {
                throw new ArgumentNullException(nameof(entity));
            }

            lock(_syncTableAccess)
            {
                var index = Table.IndexOf(Table.FirstOrDefault(e => e.ID == entity.ID));
                if(index < 0)
                {
                    return false;
                }

                Table.RemoveAt(index);
                if(index > Table.Count - 1)
                    Table.Add(entity);
                else
                    Table.Insert(index, entity);

                return true;
            }
        }
        public bool Update(int id, T entity)
        {
            entity.ID = id;

            return Update(entity);
        }
        public bool Delete(int id)
        {
            lock (_syncTableAccess)
            {
                var temp = Get(id);
                var index = temp == null ? -1 : Table.IndexOf(temp);
                if (index < 0)
                {
                    return false;
                }

                Table.RemoveAt(index);

                return true;
            }
        }
        public bool Delete(T entity)
        {
            if (entity == null)
            {
                throw new ArgumentNullException(nameof(entity));
            }

            return Delete(entity.ID);
        }

        private List<T> CreatePopulatedList()
        {
            var data = new List<T>();
            for(var i = 1; i <= GetTablePopulateRowsCount(); i++)
            {
                var entity = CreateSample(i);
                entity.ID = i;

                data.Add(entity);
            }

            return data;
        }

        protected abstract int GetTablePopulateRowsCount();
        protected abstract int GetMaxTableRowsCount();
        protected abstract T CreateSample(int identity);
    }
}