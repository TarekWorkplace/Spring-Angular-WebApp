
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import os
from datetime import date


today_date = date.today()
folder_name = today_date.strftime("%Y-%m-%d")

if not os.path.exists(folder_name):
    os.makedirs(folder_name)





root_path = 'src/main/resources/Data/'

customers_df = pd.read_csv(root_path + 'olist_customers_dataset.csv')
items_df = pd.read_csv(root_path + 'olist_order_items_dataset.csv')
payments_df = pd.read_csv(root_path + 'olist_order_payments_dataset.csv')
orders_df = pd.read_csv(root_path + 'olist_orders_dataset.csv')
products_df = pd.read_csv(root_path + 'olist_products_dataset.csv')
sellers_df = pd.read_csv(root_path + 'olist_sellers_dataset.csv')
categories_df = pd.read_csv(root_path + 'product_category_name_translation.csv')


# # Data Analysis

# In[ ]:


customers_df.head(2)


# In[ ]:


items_df.head(2)


# In[ ]:


payments_df.head(2)


# In[ ]:


orders_df.head(2)


# In[ ]:


products_df.head(2)


# In[ ]:


sellers_df.head(2)


# In[ ]:


categories_df.head(2)


# In[ ]:


dataframes = {'customers': customers_df,
              'items': items_df, 
              'payments': payments_df, 
              'orders': orders_df, 
              'products': products_df, 
              'sellers': sellers_df, 
              'categories': categories_df}
for i, j in dataframes.items():
    print(f'{i:12s} dataframe: {str(len(j)):7s} rows')


# **Merge categories_df and product_df dataframes to use the English names**

# In[ ]:


# products_df contains 73 unique categories, while categories_df contains 71: that's why we use left, 
# for missing categories we keep the category name in Portuguese.
products_df = pd.merge(products_df, categories_df, on='product_category_name', how='left')
# Delete 'product_category_name' column
del products_df['product_category_name']
# Delete  the categories_df dataframe
del categories_df
# Rename the column
products_df.rename(columns={'product_category_name_english': 'product_category'}, inplace=True)


# **How many customers, orders, and orders per customer do we have?**

# In[ ]:


customers = customers_df['customer_unique_id'].nunique()
orders = orders_df.order_id.nunique()
print("number of customers:", customers)
print("number of orders:   ", orders)
print(f"number of orders per cusotmer: {orders / customers:.2f}")


# Almost all customers made only 1 order.
# 
# **Merge all other dataframes**

# In[ ]:


df = pd.merge(orders_df, customers_df, on='customer_id')
df = df.merge(items_df, on='order_id')
df = df.merge(payments_df, on='order_id')
df = df.merge(products_df, on='product_id')
df = df.merge(sellers_df, on='seller_id')
df.head(3)


# **Customers by state**

# In[ ]:


customer_by_state = df[['customer_unique_id', 'customer_state']].groupby('customer_state').count().reset_index()
customer_by_state = customer_by_state.sort_values(by=['customer_unique_id'])

plt.style.use('seaborn-v0_8')
plt.figure(figsize=(15,10))
plt.bar(customer_by_state['customer_state'], customer_by_state['customer_unique_id'])
image_path = os.path.join(folder_name, 'customer_by_state.png')
plt.savefig(image_path)




# We can see that most customers are from SP (I think this is Sao Paulo)
# 
# **Number of orders per year and month**

# In[ ]:


# We 3 new columns
df['order_purchase_year'] = pd.to_datetime(df['order_purchase_timestamp']).dt.year
df['order_purchase_month'] = pd.to_datetime(df['order_purchase_timestamp']).dt.month
df['order_purchase_day'] = pd.to_datetime(df['order_purchase_timestamp']).dt.day
df['order_purchase_hour'] = pd.to_datetime(df['order_purchase_timestamp']).dt.hour

orders = df[['order_id', 'order_purchase_year', 'order_purchase_month']]
orders = orders.groupby(['order_purchase_month', 'order_purchase_year']).count().reset_index()
orders = orders.sort_values(by=['order_purchase_year', 'order_purchase_month'])
orders["period"] =  orders["order_purchase_month"].astype(str) + "/" + orders["order_purchase_year"].astype(str)
orders.head(3)


# In[ ]:


plt.figure(figsize=(15,10))
plt.bar(orders['period'], orders['order_id'])
plt.xticks(rotation=75, fontsize=15, weight='bold')
plt.yticks(fontsize=15, weight='bold')
image_path = os.path.join(folder_name, 'Number of orders per year and month.png')
plt.savefig(image_path)



# We can see that the year 2018 was the year with the most orders, let's confirm that:

# In[ ]:


orders.groupby(['order_purchase_year']).sum()


# **Top 10 categories**

# In[ ]:


top_categories = df[['product_category', 'order_item_id']]
top_categories = top_categories.groupby(['product_category']).sum().sort_values(by=['order_item_id'], ascending=False).reset_index()
top_categories[:10]


# In[ ]:


plt.figure(figsize=(15,10))
plt.bar(top_categories['product_category'][:10], top_categories['order_item_id'][:10])
plt.title('Number of products sold per category')
plt.xticks(rotation=75, fontsize=15, weight='bold')
plt.yticks(fontsize=15, weight='bold')
image_path = os.path.join(folder_name, 'Top 10 categories.png')
plt.savefig(image_path)



# # Prepare the Data for the Model
# 
# Let's select the attributes that we want to keep:

# In[ ]:


columns = ['order_status', 'customer_state', 'order_item_id', 'price', 
           'freight_value', 'payment_sequential', 'payment_type', 'payment_installments', 'payment_value', 
           'order_purchase_year', 'order_purchase_month', 'order_purchase_day', 'order_purchase_hour']
df = df[columns]
df.head()


# In[ ]:


df.info()


# In[ ]:


df.describe()


# **Check for null values**

# In[ ]:


df.isnull().any()


# ## Build a full pipeline for preprocessing numerical and categorical attributes
# 
# We have 3 categorical attributes: order_status, customer_state, and payment_type. We will convert these categories using Scikit-learn's OneHotEncoder class:

# In[ ]:


from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.pipeline import Pipeline
from sklearn.compose import ColumnTransformer


num_attributes = ['order_item_id', 'price', 'freight_value', 'payment_sequential', 'payment_installments', 
                  'payment_value', 'order_purchase_year', 'order_purchase_month', 'order_purchase_day', 'order_purchase_hour']
cat_attributes = ['order_status', 'customer_state', 'payment_type']

pipeline = ColumnTransformer([
        ('num', StandardScaler(), num_attributes),
        ('cat', OneHotEncoder(), cat_attributes),
])
df_prepared = pipeline.fit_transform(df)
df_prepared


# In[ ]:


df_prepared.shape


# The output is a sparse matrix. This is useful when there are thousands of categories because onehot encoding converts these categories to a matrix full of zeros except for a single 1 per row. storing such a matrix takes to much memory, so instead a sparse matrix only stores the location of the nonzero elements.
# 
# In our case, we need to convert this sparse matrix to a numpy array because we need to perform a dimensionality reduction with PCA (PCA does not support sparse input):

# In[ ]:


df_prepared = df_prepared.toarray()


# ### Dimensionality Reduction

# In[ ]:


from sklearn.decomposition import PCA

# Preserving 95% of the variance
pca = PCA(n_components=0.95)
df_reduced = pca.fit_transform(df_prepared)
df_reduced.shape


# ### Clustring Using KMeans
# 
# first, we will plot the inertia as a function of the number of clusters to get a quick overview of the optimal number of clusters

# In[ ]:


from sklearn.cluster import KMeans

k_range = range(2, 15)
kmeans_per_k = [KMeans(n_clusters=k, random_state=42).fit(df_reduced)
                for k in k_range]
inertias = [model.inertia_ for model in kmeans_per_k]


# In[ ]:


plt.figure(figsize=(15, 8))
plt.plot(k_range, inertias, 'bo-')
plt.xlabel('K', fontsize=16)
plt.ylabel('Inertia', fontsize=16)
image_path = os.path.join(folder_name, 'Clustring.png')
plt.savefig(image_path)



# It looks like that the optimal number of cluster on this inertia diagram is somewhere between 5 and 10

# In[ ]:


from sklearn.metrics import silhouette_score

silhouette_scores = [silhouette_score(df_reduced, model.labels_)
                      for model in kmeans_per_k]


# In[ ]:


best_index = np.argmax(silhouette_scores)
best_k = k_range[best_index]
best_score = silhouette_scores[best_index]
# Best number of clusters
best_k


# In[ ]:


plt.figure(figsize=(10, 5))
plt.plot(range(2, 15), silhouette_scores, "bo-")
plt.xlabel("k", fontsize=14)
plt.ylabel("Silhouette score", fontsize=14)
plt.plot(best_k, best_score, 'rs')
image_path = os.path.join(folder_name, 'Best number of clusters.png')
plt.savefig(image_path)



# We can see that the best number of clusters is 4, let's pick the best model:

# In[ ]:


best_model = kmeans_per_k[best_index]
best_model


# In[ ]:


y_pred = best_model.fit_predict(df_reduced)


# In[ ]:


for i in range(best_k):
    print(f"cluster {i + 1} contains: {np.sum(y_pred == i)} customers")


# Now that we have the clusters, we can get the average depense per cluster, the preferred category of each cluster (I removed the product categories due to the high computational cost) and so on and, for example, do some advertisements or offers specific to each cluster.
# 
# Finally, let's try to plot each cluster using TSNE

# In[ ]:


from sklearn.manifold import TSNE

tsne = TSNE(n_components=2, random_state=42)
df_reduced = tsne.fit_transform(df_prepared)


# In[ ]:


plt.figure(figsize=(13,10 ))
plt.scatter(df_reduced[:, 0], df_reduced[:, 1],c=y_pred, cmap="jet")
plt.axis('off')
plt.colorbar()
image_path = os.path.join(folder_name, ' TSNE.png')
plt.savefig(image_path)


# In[ ]:




