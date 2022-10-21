import APP_URL from '../../app.url.json';

export const items = [
  {
    label: 'Create User',
    link: 'user-creator',
    icon: 'account_circle',
    role: 'service-requester',
    internal: true,
    enable: false
  },
  {
    label: 'Send Request',
    link: 'requester',
    icon: 'chat_bubble_outline',
    role: 'service-requester',
    internal: true,
    enable: true
  },
  {
    label: 'Requests in Process',
    link: 'requests-in-process',
    icon: 'hourglass_empty',
    role: 'service-requester',
    internal: true,
    enable: true
  },
  {
    label: 'Offers',
    link: 'offers',
    icon: 'shopping_cart',
    role: 'service-requester',
    internal: true,
    enable: true
  },
  {
    label: 'My Services',
    link: 'my-services',
    icon: 'how_to_reg',
    role: 'service-requester',
    internal: true,
    enable: true
  },
  {
    label: 'Verification Results',
    link: 'verification-results',
    icon: 'assignment_turned_in',
    role: 'otf-provider',
    internal: true,
    enable: true
  },
  {
    label: 'Basic Services',
    link: 'services',
    icon: 'extension',
    role: 'service-provider',
    internal: true,
    enable: true
  },
  {
    label: 'Rate Service',
    link: 'rate',
    icon: 'star',
    role: 'service-requester',
    internal: true,
    enable: true
  },
  {
    label: 'View Ratings',
    link: 'ratings',
    icon: 'stars',
    role: 'service-requester',
    internal: true,
    enable: true
  },
  {
    label: 'Executors',
    link: 'executors',
    icon: 'storage',
    role: 'compute-center',
    internal: true,
    enable: true
  },
  {
    label: 'Monitor',
    link: APP_URL.MONITOR,
    icon: 'dvr',
    role: 'market-provider',
    internal: false,
    enable: true
  },
  {
    label: 'OTF Provider Network',
    link: 'otf-provider-network',
    icon: 'device_hub',
    role: 'market-provider',
    internal: true,
    enable: true
  },
];
